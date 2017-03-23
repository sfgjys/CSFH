package com.minji.cufcs;

import android.support.v4.app.Fragment;

import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.fragment.FragmentArea;
import com.minji.cufcs.fragment.FragmentAutonomously;
import com.minji.cufcs.fragment.FragmentHomePage;
import com.minji.cufcs.fragment.FragmentMe;
import com.minji.cufcs.fragment.FragmentRain;
import com.minji.cufcs.fragment.FragmentSetting;
import com.minji.cufcs.fragment.FragmentUnit;
import com.minji.cufcs.fragment.FragmentValve;
import com.minji.cufcs.fragment.FragmentWater;
import com.minji.cufcs.observer.MySubject;

public class FragmentFactory {

	public static BaseFragment[] fragments = new BaseFragment[2];

	public static Fragment create(int position, String differentiate) {
		BaseFragment fragment = null;

		if ("Work".equals(differentiate)) {
			switch (position) {
			case 0:
				fragment = new FragmentUnit();
				break;
			case 1:
				fragment = new FragmentValve();
				break;
			}
		}
		if ("WaterRain".equals(differentiate)) {
			switch (position) {
			case 0:
				fragment = new FragmentWater();
				break;
			case 1:
				fragment = new FragmentRain();
				break;
			}
		}
		if ("Implement".equals(differentiate)) {
			switch (position) {
			case 0:

				FragmentArea fragmentArea = new FragmentArea();
				fragment = fragmentArea;
				MySubject.getInstance().add(fragmentArea);

				break;
			case 1:
				FragmentAutonomously autonomously = new FragmentAutonomously();
				fragment = autonomously;
				MySubject.getInstance().add(autonomously);
				break;
			}
		}

		fragments[position] = fragment;

		return fragment;
	}
}
