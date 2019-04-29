package com.claire.traveldiary.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.claire.traveldiary.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void showOrAddFragmentByTag(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, String fragmentTag) {
        checkNotNull(fragmentManager);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_container, fragment, fragmentTag);
        }

        transaction.commit();
    }

    public static void addFragmentByTag(@NonNull FragmentManager fragmentManager,
                                        @NonNull Fragment fragment, String fragmentTag) {
        checkNotNull(fragmentManager);

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (Fragment element : fragmentManager.getFragments()) {
            if (!element.isHidden()) {
                transaction.hide(element);
                transaction.addToBackStack(null);
                break;
            }
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.layout_container, fragment, fragmentTag);
        }

        transaction.commit();
    }
}
