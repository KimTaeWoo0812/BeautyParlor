package beauty.tw.beauty_mother.a6_master_Package;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_sub_class.BackPressCloseHandler;


/**
 * Created by kimtaewoo on 2017-01-19.
 */
public class Fragment_main extends AppCompatActivity {
    ComponentName beaconServiceVar;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_fragment_main);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
        moveTaskToBack(true);
    }
    public void Dialog_ForBackPressed(){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("앱을 종료하시겠습니까?").setCancelable(
                false).setPositiveButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).setNegativeButton("종료",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("앱 종료");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.logo);
        alert.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (beaconServiceVar == null) {
            return;
        }
        Intent i = new Intent();
        i.setComponent(beaconServiceVar);
        if (stopService(i)) {
            Log.e("MMA_Seongho", "Service Stopped!!");
            Toast.makeText(getApplicationContext(), "비콘 감지 서비스 종료됨", Toast.LENGTH_SHORT);
        } else {
            Log.e("MMA_Seongho", "Service ALREADY Stopped!!");
            Toast.makeText(getApplicationContext(), "비콘 감지 서비스 이미 종료됨", Toast.LENGTH_SHORT);
        }


    }

//    @Override
//    protected void onPause(){
//        for (int i = 0; i < SchoolData.carList.size(); i++) {
//            UDP_SC.SendMsg("STOP" + UDP_SC._del + SchoolData.carList.get(i)+ UDP_SC._del +"메인페이지");
//            Log.e("Map:  ", SchoolData.carList.get(i));
//            Log.e("Map: -> ", UDP_SC._del);
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        super.onPause();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new Fragment_bookingList();
                case 1:
                    return new Fragment_Setting();
                default:
                    return new Fragment_bookingList();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "학 원   홈";
                case 1:
                    return "차 량   위 치";
            }
            return null;
        }
    }

}
