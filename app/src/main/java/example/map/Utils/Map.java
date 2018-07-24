package example.map.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.internal.PlacemarkMapObjectBinding;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class Map implements LocationListener {
    public static float ZOOM = 80f;
    public static long PERIOD = 2000l;
    public static float MINDISTANCE = 0.05f;
    public static float DURATION = 5f;
    private static ImageProvider USER_IMAGE = null;

    public static Location currentLocation;
    private static PlacemarkMapObject user;

    private static Context ctx;
    private static MapView mv;

    private static Point getPoint(Location loc) {
        return new Point(loc.getLatitude(), loc.getLongitude());
    }

    public static void SetUp(Context context, MapView mapView, ImageProvider userIcon)
    {
        ctx = context;
        mv = mapView;
        USER_IMAGE = userIcon;


        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new Map();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    PERIOD,
                    MINDISTANCE,
                    locationListener);

            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setNewPositionOfUser();
            moveCamera(currentLocation);
        }
    }

    public static void moveCameraToCurrentPosition() {
        moveCamera(currentLocation);
    }

    public static void moveCamera(Location location) {
        if (mv != null) {
            mv.getMap().move(
                    new CameraPosition(getPoint(location), ZOOM, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, DURATION),
                    null);
        }
    }

    private static void setNewPositionOfUser() {
        if (user != null) {
            user.setGeometry(getPoint(currentLocation));
        }
        else {
            if (USER_IMAGE != null) {
                user = mv.getMap().getMapObjects().addPlacemark(getPoint(currentLocation), USER_IMAGE);
            } else {
                user = mv.getMap().getMapObjects().addPlacemark(getPoint(currentLocation));
            }
        }
    }

    public static void setMarker(double lat, double lon) {
        Point point = new Point(lat, lon);
        mv.getMap().getMapObjects().addPlacemark(point);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        setNewPositionOfUser();

        //TODO потом можно будет убрать след строку и сделать кнопку по нажатию которой будет вызываться следующий метод
        moveCameraToCurrentPosition();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //Log.d("Map (onStatusChanged)", s);
    }

    @Override
    public void onProviderEnabled(String s) {
        //Log.d("Map (onProviderEnabled)", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        //Log.d("Map (onProviderDisabled)", s);
    }
}
