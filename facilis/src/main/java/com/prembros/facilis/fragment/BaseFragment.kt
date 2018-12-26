package com.prembros.facilis.fragment

import androidx.fragment.app.Fragment

/**
 * Base class for all the fragments in the app
 * Contains the functionality parts common through all the fragments.
 */
abstract class BaseFragment : Fragment() {

    /**
     * Function for child fragment to notify the activity whether they are ready to exit or not
     * @return false when the fragment needs to do it's own actions when back is pressed, true otherwise
     */
    open fun onBackPressed(): Boolean = true
}