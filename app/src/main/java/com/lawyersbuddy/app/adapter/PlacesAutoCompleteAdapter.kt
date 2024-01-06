//package com.demopush.lawyersreminder.adapter
//
//import android.content.Context
//import android.graphics.Typeface
//import android.text.style.CharacterStyle
//import android.text.style.StyleSpan
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Filter
//import android.widget.Filterable
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.NonNull
//import androidx.recyclerview.widget.RecyclerView
//import com.demopush.lawyersreminder.R
//import com.demopush.lawyersreminder.data.ApiException
//import com.google.android.gms.tasks.Task
//import com.google.android.gms.tasks.Tasks
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.AutocompleteSessionToken
//import com.google.android.libraries.places.api.net.FetchPlaceRequest
//import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
//import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
//import com.google.android.libraries.places.api.net.PlacesClient
//import java.util.concurrent.ExecutionException
//import java.util.concurrent.TimeUnit
//import java.util.concurrent.TimeoutException
//
//class PlacesAutoCompleteAdapter(context: Context) :
//    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {
//    private var mResultList: ArrayList<PlaceAutocomplete>? = ArrayList()
//    private val mContext: Context
//    private val STYLE_BOLD: CharacterStyle
//    private val STYLE_NORMAL: CharacterStyle
//    private val placesClient: PlacesClient
//
//
//    init {
//        mContext = context
//        STYLE_BOLD = StyleSpan(Typeface.BOLD)
//        STYLE_NORMAL = StyleSpan(Typeface.NORMAL)
//        placesClient = Places.createClient(context)
//    }
//    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {
//        val resultList: ArrayList<PlaceAutocomplete> = ArrayList()
//        val token = AutocompleteSessionToken.newInstance()
//
////https://gist.github.com/graydon/11198540
//// Use the builder to create a FindAutocompletePredictionsRequest.
//        val request =
//            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
//                //.setLocationBias(bounds)
//                .setCountry("IN")
//                //.setTypeFilter(TypeFilter.ADDRESS)
//                .setSessionToken(token)
//                .setQuery(constraint.toString())
//                .build()
//        val autocompletePredictions: Task<FindAutocompletePredictionsResponse> =
//            placesClient.findAutocompletePredictions(request)
//
//        try {
//            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
//        } catch (e: ExecutionException) {
//            e.printStackTrace()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        } catch (e: TimeoutException) {
//            e.printStackTrace()
//        }
//        return if (autocompletePredictions.isSuccessful()) {
//            val findAutocompletePredictionsResponse: FindAutocompletePredictionsResponse =
//                autocompletePredictions.getResult()
//            if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
//                Log.i(TAG, prediction.placeId)
//                resultList.add(
//                    PlaceAutocomplete( )
//                )
//            }
//            resultList
//        } else {
//            resultList
//        }
//    }
//    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): PredictionHolder {
//        val layoutInflater =
//            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val convertView: View =
//            layoutInflater.inflate(R.layout.client_name_list_itemview, viewGroup, false)
//        return PredictionHolder(convertView)
//    }
//
//    override fun onBindViewHolder(@NonNull mPredictionHolder: PredictionHolder, i: Int) {
//        mPredictionHolder.tv_client_name.text = mResultList!![i].address
//    }
//    override fun getItemCount(): Int {
//        return mResultList!!.size
//    }
//
//    fun getItem(position: Int): PlaceAutocomplete {
//        return mResultList!![position]
//    }
//
//    inner class PredictionHolder internal constructor(itemView: View) :
//        RecyclerView.ViewHolder(itemView),
//        View.OnClickListener {
//
//
//        init {
//
//            itemView.setOnClickListener(this)
//        }
//        override fun onClick(v: View?) {
//            try {
//
//                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
//                placesClient.fetchPlace(request).addOnSuccessListener { response ->
//                    val place = response.place
//
////                (mContext as PlaceSearchActivity).address(place)
//
//                }.addOnFailureListener { exception ->
//                    if (exception is ApiException) {
//                        Toast.makeText(mContext, exception.message + "", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//        }
//    }
//
//    inner class PlaceAutocomplete internal constructor(
//        var tv_client_name: TextView) {
//    }
//    companion object {
//        private const val TAG = "PlacesAutoAdapter"
//    }
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence): FilterResults {
//                val results = FilterResults()
//                // Skip the autocomplete query if no constraints are given.
//                if (constraint != null) {
//                    // Query the autocomplete API for the (constraint) search string.
//                    mResultList = getPredictions(constraint)
//                    if (mResultList != null) {
//                        // The API successfully returned results.
//                        results.values = mResultList
//                        results.count = mResultList!!.size
//                    }
//                }
//                return results
//            }
//
//            override fun publishResults(constraint: CharSequence, results: FilterResults) {
//                if (results != null && results.count > 0) {
//                    // The API returned at least one result, update the data.
//                    notifyDataSetChanged()
//                } else {
//                    // The API did not return any results, invalidate the data set.
//                    //notifyDataSetInvalidated();
//                }
//            }
//        }
//    }
//
//}