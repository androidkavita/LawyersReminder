package com.lawyersbuddy.app.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.MenuListAdapter
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityMainBinding
import com.lawyersbuddy.app.fragments.HomeFragment
import com.lawyersbuddy.app.fragments.TodayCalendarFragment
import com.lawyersbuddy.app.model.DashboardMenuModel
import com.lawyersbuddy.app.util.AppConstant
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.ConstantBottom
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.GetProfileViewModel
import com.lawyersbuddy.app.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(),   View.OnClickListener{
    private lateinit var binding : ActivityMainBinding
    private val viewModel: GetProfileViewModel by viewModels()
    private val SettingViewModel: NotificationViewModel by viewModels()

    private var menuList: ArrayList<DashboardMenuModel>?  = null
    lateinit var frag: Fragment
    private var backPressed = true
    private var menuListAdapter: MenuListAdapter? =  null
    private var exit: Boolean = false
    lateinit var bottomSheetLogout: BottomSheetDialog

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onStart() {
        super.onStart()

        setNavigationData()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        SettingViewModel.notification_status("Bearer " + userPref.getToken().toString())

//        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigationView.setItemIconTintList(null);

        replaceFragment(HomeFragment())
        viewModel.errorString.observe(this) {
            toast(it)
        }
        if (!userPref.getUserProfileImage().isNullOrBlank()) {
            Glide.with(this).load(Uri.parse(userPref.getUserProfileImage()))
                .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                .into(binding.drawerToolbar.ivHeaderpic)
            Glide.with(this).load(Uri.parse(userPref.getUserProfileImage()))
                .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                .into(binding.header.imgUser)
        }
        viewModel.get_profileApi("Bearer " + userPref.getToken().toString())

        viewModel.profileViewResponse.observe(this) {
            if (it.status == 1) {

                Glide.with(this).load(it.data!!.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                    .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                    .into(binding.drawerToolbar.ivHeaderpic)
                Glide.with(this).load(it.data!!.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                    .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                    .into(binding.header.imgUser)

            } else {
                Log.d("Response", it.toString())
                toast(this, it.message!!)
            }
        }
        SettingViewModel.notificationTypeResponse.observe(this){
            if (it.status==1){
                userPref.setNotificationType(it.data.notification_status.toString())
            }else{
                toast(it.message)
            }
        }

        binding.drawerToolbar.ivHeaderpic.setOnClickListener {
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        menuList = ArrayList<DashboardMenuModel>()

        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }
            override fun onDrawerOpened(drawerView: View) {

                //setNavigationData();
            }
            override fun onDrawerClosed(drawerView: View) {

            }
            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        binding.drawerToolbar.ivMenu.setOnClickListener {
            // open drawer here
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        setNavigationBar()
        prepareNavMenuList()
        navMenuClickListener()
        initializeUsersBnv()

    }
    @SuppressLint("SetTextI18n")
    private fun setNavigationData() {
        binding.header.tvUserName.text = userPref.getName()
        binding.header.tvEmail.text = userPref.getEmail()

        if (!userPref.getUserProfileImage().isNullOrBlank()) {
            Glide.with(this).load(Uri.parse(userPref.getUserProfileImage()))
                .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                .into(binding.header.imgUser)
        }
    }
    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.iv_menu -> {

                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(Gravity.LEFT)

                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT)
                }
            }

        }
    }





    private fun setNavigationBar() {
        frag = HomeFragment()
        CommonUtils.setFragment(frag, true, this, R.id.flContent)
        setBottomNavigation()

    }
    private fun prepareNavMenuList() {

        menuList!!.clear()
        if (userPref.getType().equals("1")){
            menuList!!.add(DashboardMenuModel(this.getString(R.string.myprofile),R.drawable.ic_myprofile))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.mysubscription),R.drawable.bxs_offer))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.mypayment),R.drawable.ic_mypayment))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.settings),R.drawable.ic_settings))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.notifications),R.drawable.ic_notifications))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.aboutus),R.drawable.ic_aboutus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.whyus),R.drawable.ic_whyus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.contactus),R.drawable.ic_contactus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.logout),R.drawable.ic_logout))
        }
        else{
            menuList!!.add(DashboardMenuModel(this.getString(R.string.myprofile),R.drawable.ic_myprofile))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.notifications),R.drawable.ic_notifications))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.aboutus),R.drawable.ic_aboutus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.whyus),R.drawable.ic_whyus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.contactus),R.drawable.ic_contactus))
            menuList!!.add(DashboardMenuModel(this.getString(R.string.logout),R.drawable.ic_logout))
        }


        menuListAdapter = MenuListAdapter(this, menuList!!)
        binding.listVideMenu.adapter = menuListAdapter
    }
//    fun setBottomNavigation() {
//        binding.bottomNavigationView.setOnNavigationItemSelectedListener()
//    }





    private fun navMenuClickListener() {
        if (userPref.getType().equals("1")) {
            binding.listVideMenu.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    when (position) {
                        0 -> {
                            val intent = Intent(this, MyProfileActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this, MySubscriptionOffer::class.java)
                            startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(this, MyPaymentsActivity::class.java)
                            startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(this, SettingActivity::class.java).putExtra("view","viewsetting")
                            startActivity(intent)
                        }
                        4 -> {
                            val intent = Intent(this, NotificationActivity::class.java)
                            startActivity(intent)
                        }
                        5 -> {
                            val intent = Intent(this, com.lawyersbuddy.app.activities.Aboutus::class.java)
                            startActivity(intent)
                        }
                        6 -> {
                            val intent = Intent(this, WhyUs::class.java)
                            startActivity(intent)
                        }
                        7 -> {
                            val intent = Intent(this, Contactus::class.java)
                            startActivity(intent)
                        }
                        8 -> {
                            logoutDialog()
                        }
                    }
                    closeDrawer()
                }
        }else{
            binding.listVideMenu.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    when (position) {
                        0 -> {
                            val intent = Intent(this, MyProfileActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(this, NotificationActivity::class.java)
                            startActivity(intent)
                        }
                        2 ->
                        {
                            val intent = Intent(this, com.lawyersbuddy.app.activities.Aboutus::class.java)
                            startActivity(intent)
                        }
                        3 -> {
                            val intent = Intent(this, WhyUs::class.java)
                            startActivity(intent)
                        }
                        4 -> {
                            val intent = Intent(this, Contactus::class.java)
                            startActivity(intent)
                        }
                        5 -> {
                            logoutDialog()
                        }
                    }
                    closeDrawer()
                }

        }
    }
    private fun closeDrawer() {
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }


    fun setBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
    fun AddMenusInBnv(
        groupId: Int,
        menuId: Int,
        order: Int,
        menuTitle: String,
        icon: Int
    ) {
        var menu: Menu = binding.bottomNavigationView.menu
        menu.add(groupId, menuId, order, menuTitle)
        menu.findItem(menuId).setIcon(icon)
    }

    fun redirectToTodayTab(tab: Int) {
        binding.bottomNavigationView.menu.getItem(tab).isChecked = true
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                ConstantBottom.Dashboard.HOME -> {
                    redirectToTodayTab(0)
                    if (frag is HomeFragment) {

                    } else {
                        SwitchFragment(ConstantBottom.Dashboard.HOME)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                ConstantBottom.Dashboard.TODAYCALEDER -> {
                    redirectToTodayTab(1)

                    if (frag is TodayCalendarFragment) {

                    } else {
                        SwitchFragment(ConstantBottom.Dashboard.TODAYCALEDER)
                        return@OnNavigationItemSelectedListener true
                    }
                }

            }
            false
        }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
//            binding.bottomNavigationView.menu.getItem(0).isChecked = true
//            frag = HomeFragment()
//            CommonUtils.setFragment(frag, false, this, R.id.flContent)
        } else {
            if (frag is HomeFragment) {
                if (exit) {
                    super.onBackPressed()
                    finishAffinity()
                    //finish() // finish activity
                } else {
                    Handler().postDelayed({
                        toast(getString(R.string.pressbackagain))
                        exit = true

                    }, 3 * 100)
                }
            } else {
                binding.bottomNavigationView.menu.getItem(0).isChecked = true
                frag = HomeFragment()
                CommonUtils.setFragment(frag, false, this, R.id.flContent)
            }
        }
    }
    open fun initializeUsersBnv() {
        AddMenusInBnv(
            Menu.NONE, ConstantBottom.Dashboard.HOME,
            0, "Home",
            R.drawable.homebuttonxml
        )
        AddMenusInBnv(
            Menu.NONE,ConstantBottom.Dashboard.TODAYCALEDER,
            1, "Today's Event",
            R.drawable.calbuttonxml
        )

        //SwitchFragment(Constants.DriverDashboardBottomNavTab.HOME)
    }
    private fun SwitchFragment(tab: Int) {
        when (tab) {
            ConstantBottom.Dashboard.HOME -> {
                AppConstant.tabIndex = 0
                frag = HomeFragment()
                CommonUtils.setFragment(frag, false, this, R.id.flContent)
            }
            ConstantBottom.Dashboard.TODAYCALEDER -> {
                AppConstant.tabIndex = 1
                frag = TodayCalendarFragment()
                CommonUtils.setFragment(frag, false, this, R.id.flContent)
            }
        }
    }



    private fun logoutDialog() {
        val dialogBinding = LayoutInflater
            .from(this).inflate(R.layout.dialog_logout, null)
        bottomSheetLogout = BottomSheetDialog(this)
        bottomSheetLogout.setContentView(dialogBinding)

        bottomSheetLogout.setOnShowListener { dia ->
            val bottomSheetDialog = dia as BottomSheetDialog
            val bottomSheetInternal: FrameLayout =
                bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheetInternal.setBackgroundResource(R.drawable.ic_launcher_background)
        }
        bottomSheetLogout.setCancelable(true)

        val btnclose: Button = bottomSheetLogout.findViewById(R.id.btnCancel)!!
        val btnlogout: Button = bottomSheetLogout.findViewById(R.id.btnLogout)!!



        btnclose.setOnClickListener {
            bottomSheetLogout.dismiss()
        }
        btnlogout.setOnClickListener {
            userPref.isLogin=false
            userPref.clearPref()
            finishAffinity()
            startActivity(Intent(this, ChooseRoleActivity::class.java))
            bottomSheetLogout.dismiss()
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut()
        }
        bottomSheetLogout.show()

    }

    override fun onResume() {
        super.onResume()
        viewModel.get_profileApi("Bearer " + userPref.getToken().toString())
    }



}