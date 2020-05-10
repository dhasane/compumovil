
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterapp/listaPaises.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong/latlong.dart';

class mapView extends StatelessWidget {

  final Pais pais;

  mapView({Key key, @required this.pais}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;
    double heigh = screenSize.height;
    TextStyle whiteStyle = new TextStyle(fontSize: 20.0, color: Colors.white);
    return new Directionality(
      textDirection: TextDirection.rtl,
      child: new Container(
          padding: new EdgeInsets.only(bottom: 10.0, left: 1.0, right: 1.0),
          color: Colors.white,
          child: new FlutterMap(
            options: new MapOptions(
                center: new LatLng(pais.getLatlng[0], pais.getLatlng[1]),
                zoom: 5.0,
                maxZoom: 15.0,
                minZoom: 3.0,
            ),
            layers: [
              new TileLayerOptions(
                  urlTemplate:
                      "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
                  subdomains: ['a', 'b', 'c']),
//              new MarkerLayerOptions(markers: markers)
            ],
          )
      )
    );
  }
}

