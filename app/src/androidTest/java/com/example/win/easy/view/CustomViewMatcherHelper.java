package com.example.win.easy.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

@SuppressWarnings("ALL")
public class CustomViewMatcherHelper {

    public static Matcher<View> nthChildOf(Matcher<View> parentMatcher, int index){
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                ViewParent parentOfThis=item.getParent();
                ViewGroup parentOfThisAsViewGroup;
                if (parentOfThis instanceof ViewGroup)
                    parentOfThisAsViewGroup=(ViewGroup)parentOfThis;
                else
                    return false;

                return parentMatcher.matches(parentOfThisAsViewGroup)
                        &&parentOfThisAsViewGroup.getChildCount()>index
                        &&parentOfThisAsViewGroup.getChildAt(index).equals(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(index+"th child of :");
                parentMatcher.describeTo(description);
            }
        };
    }
}
