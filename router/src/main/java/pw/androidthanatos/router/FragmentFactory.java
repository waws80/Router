package pw.androidthanatos.router;

import android.os.Bundle;

/**
 * fragment factory
 *
 * Created by liuxiongfei on 2017/10/31.
 */

public final class FragmentFactory {

    private FragmentFactory(){}

    private static final class Builder{
        private static final FragmentFactory FACTORY = new FragmentFactory();
        private static final DelegateFragment DEFAULT = new DelegateFragment();
        private static final DelegateFragmentV4 V4 = new DelegateFragmentV4();
    }

    public static FragmentFactory getDefault(){
        return Builder.FACTORY;
    }

    public DelegateFragmentV4 getV4(){
        DelegateFragmentV4 fragment = Builder.V4;
        //fragment.setArguments(new Bundle());
        return Builder.V4;
    }

    public DelegateFragment get(){
        return Builder.DEFAULT;
    }

}
