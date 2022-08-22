package com.example.pointmap

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import com.example.pointmap.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider

class MapFragment : Fragment(), UserLocationObjectListener, InputListener {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding get() = _binding!!

    private var userLocationLayer: UserLocationLayer? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            getUserLocation()
        }
        else showRationaleDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initUserLocationLayer()
        checkLocationPermission()
    }

    override fun onStart() {
        super.onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initMap() {
        binding.mapview.map.apply {
//            isRotateGesturesEnabled = false
            addTapListener { tapEvent ->
                val selectionMetadata =
                    tapEvent.geoObject.metadataContainer.getItem(GeoObjectSelectionMetadata::class.java)
                selectionMetadata?.let {
                    binding.mapview.map.selectGeoObject(it.id, it.layerId)
                }
                selectionMetadata !=null
            }
            addInputListener(this@MapFragment)
        }
    }


    private fun checkLocationPermission() {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun getUserLocation() {
        val locationManager = requireContext().getSystemService<LocationManager>()
        val provider = locationManager?.getBestProvider(Criteria(), true)
        provider?.let {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkLocationPermission()
                return
            }
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let { moveToPosition(location) }
            locationManager.requestLocationUpdates(it, MIN_TIME, MIN_DISTANCE) { location ->
                moveToPosition(location)
            }
        }
    }

    private fun initUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance().apply {
            resetLocationManagerToDefault()
        }
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapview.mapWindow)
        userLocationLayer?.apply {
            isVisible = true
            isHeadingEnabled = true
            setObjectListener(this@MapFragment)
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.location_access_required)
            .setIcon(R.drawable.ic_warning_24)
            .setMessage(R.string.to_display_your_location_access_is_required)
            .setPositiveButton(R.string.accept) {_, _ -> checkLocationPermission()}
            .setNegativeButton(R.string.deny) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun moveToPosition(location: Location) {
        binding.mapview.map.move(
            CameraPosition(Point(location.latitude, location.longitude), 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5f),
            null
        )
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer?.setAnchor(
            PointF((binding.mapview.width*0.5f), binding.mapview.height*0.5f),
            PointF((binding.mapview.width*0.5f), binding.mapview.height*0.83f),
        )
        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(requireContext(), R.drawable.user_arrow)
        )
        val pinIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource( requireContext(), R.drawable.search_result),
            IconStyle().setAnchor(PointF(0f,0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )
    }

    override fun onMapTap(p0: Map, p1: Point) {
        p0.move(CameraPosition(p1, 14.0f, 0.0f, 0.0f))
    }

    override fun onMapLongTap(p0: Map, p1: Point) {}

    override fun onObjectRemoved(p0: UserLocationView) {}

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}

    companion object {
        private const val MIN_TIME = 100L
        private const val MIN_DISTANCE = 1f

        fun newInstance() : MapFragment = MapFragment()
    }
}