package lxnkn.bearoundwithparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(getActivity().getClass().getName().equals(AdminActivity.class.getName())){
            MenuItem login = menu.findItem(R.id.login);
            login.setVisible(false);
            MenuItem map =menu.findItem(R.id.map);
            map.setVisible(false);
            MenuItem logout = menu.findItem(R.id.logout);
            logout.setVisible(true);
        }
        else{
            if(getActivity().getClass().getName().equals(MapActivity.class.getName())){
                MenuItem login = menu.findItem(R.id.login);
                login.setVisible(true);
                MenuItem map = menu.findItem(R.id.map);
                map.setVisible(false);
                MenuItem logout = menu.findItem(R.id.logout);
                logout.setVisible(false);
            }else{
                MenuItem login = menu.findItem(R.id.login);
                login.setVisible(false);
                MenuItem map =menu.findItem(R.id.map);
                map.setVisible(true);
                MenuItem logout = menu.findItem(R.id.logout);
                logout.setVisible(false);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.login:
                startActivity(new Intent(getActivity(),LoginActivity.class));
                return true;
            case R.id.map:
                startActivity(new Intent(getActivity(),MapActivity.class));
                return true;
            case R.id.logout:
                item.setVisible(false);
                startActivity(new Intent(getActivity(),LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


}
