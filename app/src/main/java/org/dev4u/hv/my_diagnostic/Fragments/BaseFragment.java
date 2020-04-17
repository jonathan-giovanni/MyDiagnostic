package org.dev4u.hv.my_diagnostic.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    public static final String ARGS_INSTANCE = "org.dev4u.hv.my_diagnostic.argsInstance";

    FragmentNavigation mFragmentNavigation;
    int mInt = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
        public void pushFragment(Fragment fragment);
    }

}
