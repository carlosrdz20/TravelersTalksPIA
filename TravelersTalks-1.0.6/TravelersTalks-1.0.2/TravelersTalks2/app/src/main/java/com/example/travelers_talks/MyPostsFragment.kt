package com.example.travelers_talks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.travelers_talks.adapter.VPAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPostsFragment(option:Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var optionMyPosts = option

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_posts, container, false)
    }

    companion object {
        const val REQUEST_CODE_FULL_POST = 1001
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPostsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPostsFragment(0).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
        private lateinit var tabLayout: TabLayout
        private lateinit var viewPager2: ViewPager2
        private lateinit var adapter : VPAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager)

        adapter = VPAdapter(parentFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Mis Publicaciones"))
        tabLayout.addTab(tabLayout.newTab().setText("Mis Borradores"))
        tabLayout.addTab(tabLayout.newTab().setText("Mis Favoritos"))

        viewPager2.adapter = adapter

        //Checar cuando es 0 viene del menu lateral y cuando es 1 viene de fullpost page
        when(optionMyPosts){
            0->{ //seleccionamos my posts
                tabLayout.selectTab(tabLayout.getTabAt(0))
                viewPager2.currentItem = 0
            }
            1->{ //seleccionamos my posts
                tabLayout.selectTab(tabLayout.getTabAt(1))
                viewPager2.currentItem = 1
            }
            2->{ //seleccionamos my favorites
                tabLayout.selectTab(tabLayout.getTabAt(2))
                viewPager2.currentItem = 2
            }
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))

            }
        })
    }
}