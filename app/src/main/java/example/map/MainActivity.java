package example.map;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import example.map.Utils.Map;


public class MainActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("da4b9eac-ada7-4c9d-a4a4-091e56dd1c27");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapview);

        Map.SetUp(this, mapView, ImageProvider.fromResource(this, R.drawable.user));
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }
}
