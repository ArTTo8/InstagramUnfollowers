package com.artto.instagramunfollowers.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

inline fun <reified T : Fragment> createFragment(vararg args: Pair<String, Serializable>): T =
        T::class.java.newInstance()
                .apply {
                    arguments = Bundle().apply {
                        args.forEach {
                            putSerializable(it.first, it.second)
                        }
                    }
                }
