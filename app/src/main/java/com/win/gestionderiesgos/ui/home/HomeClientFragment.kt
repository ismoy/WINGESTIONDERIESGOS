package com.win.gestionderiesgos.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.win.gestionderiesgos.data.adapter.ListProjectAdapter
import com.win.gestionderiesgos.data.remote.provider.AuthProvider
import com.win.gestionderiesgos.data.remote.provider.ProjectListProvider
import com.win.gestionderiesgos.data.remote.provider.TokenProvider
import com.win.gestionderiesgos.databinding.FragmentHomeClientBinding
import com.win.gestionderiesgos.domain.model.NotificationData
import com.win.gestionderiesgos.domain.model.Project
import com.win.gestionderiesgos.domain.model.PushNotification
import com.win.gestionderiesgos.presentation.home.HomeViewModel
import com.win.gestionderiesgos.presentation.login.LoginViewModel
import com.win.gestionderiesgos.presentation.notification.NotificationViewModel
import com.win.gestionderiesgos.utils.Constants
import com.win.gestionderiesgos.utils.ShowDialog


class HomeClientFragment : Fragment() {
    private lateinit var binding: FragmentHomeClientBinding
    private lateinit var mAuthProvider: AuthProvider
    private lateinit var mTokensProvider: TokenProvider
    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private val viewModelMain by lazy { ViewModelProvider(this)[LoginViewModel::class.java] }
    private lateinit var projectAdapter: ListProjectAdapter
    private lateinit var navController: NavController
    private lateinit var lisProjectListProvider: ProjectListProvider
    private lateinit var showDialog: ShowDialog
    private val notificationViewModel by lazy { ViewModelProvider(this)[NotificationViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeClientBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = NavController(requireContext())
        mTokensProvider = TokenProvider()
        projectAdapter = ListProjectAdapter{onSectorPickingItemSelected(it)}
        mAuthProvider = AuthProvider()
        lisProjectListProvider= ProjectListProvider()
        showDialog = ShowDialog(requireContext())
        binding.recyclerView.apply {
            adapter = projectAdapter
            layoutManager = LinearLayoutManager(context)
        }

        observerViewModel()
        generateToken()
    }

    private fun observerViewModel() {
        showDialog.showDialog()
        viewModelMain.getOnlyUser(mAuthProvider.getId().toString())
        viewModelMain.responseUsers.observe(viewLifecycleOwner , Observer { user ->
            if (user.isSuccessful) {
                viewModel.getProject(mAuthProvider.getId().toString())
                viewModel.responseGetProject.observe(viewLifecycleOwner , Observer {
                   setUpRecyclerView(it)
                })
            }
        })
        viewModel.noExistProjectForUser.observe(viewLifecycleOwner, Observer { noExist->
            if (noExist){
                binding.apply {
                    noData.visibility = View.VISIBLE
                    title.visibility=View.GONE
                    recyclerView.visibility=View.GONE
                }
                showDialog.dismissDialog()
            }
        })

        viewModel.getProjectByStatus()
        viewModel.getProjectOrderByStatus.observe(viewLifecycleOwner, Observer {
            it.forEach { proje->
                if (proje.status =="Finish"){
                    sendNotificationToOtherDevice(proje.idKeyProject)

                }
            }
        })
    }

    private fun onSectorPickingItemSelected(project: Project) {
        onClickItem(project)
    }


    private fun onClickItem(project: Project) {
        requireActivity().intent.putExtra("project" , project.QuantityPercent)
        requireActivity().intent.putExtra("idProject" , project.idKeyProject)
    }

    private fun setUpRecyclerView(list: List<Project>) {
        if (list.isNotEmpty()){
            showDialog.dismissDialog()
            projectAdapter.setData(list)
            binding.noData.visibility = View.GONE
        }

    }

    private fun sendNotificationToOtherDevice(idKeyProject: String) {
        mTokensProvider.getTokenAdmin()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val tokenClient: String = ds.child("token").value.toString()
                            PushNotification(
                                NotificationData(
                                    Constants.TITLENOTIFICATION , "Un Proyecto ha sido completado "
                                ) , tokenClient
                            ).also { push ->
                                notificationViewModel.sendNotification(push)
                                notificationViewModel.responseNotification.observe(
                                    viewLifecycleOwner
                                ) {
                                  if (it.isSuccessful){
                                      Log.d("dddddddd",idKeyProject)
                                      lisProjectListProvider.updateStatusProject(idKeyProject,"Closed")
                                          .addOnCompleteListener {  }
                                  }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext() ,
                            "El usuario que estas asignando no tiene el token instalado por enden no se puede enviarle notification" ,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun generateToken() {
        mTokensProvider.createToken(mAuthProvider.getId().toString())
    }
}