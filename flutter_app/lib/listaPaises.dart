import 'package:flutter/material.dart';
import 'package:flutterapp/DetailScreen.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';


class Pais {
  final String name;
  final String capital;
  final int poblacion;
  final List<dynamic> moneda;
  final double area;
  final double gini;
  final List<dynamic> latlng;
  final String bandera;

  Pais({
    this.name,
    this.capital,
    this.poblacion,
    this.moneda,
    this.area,
    this.gini,
    this.latlng,
    this.bandera
  });

  factory Pais.fromJson(Map<String, dynamic> json) {
    return Pais(
      name     : json['name'],
      capital  : json['capital'],
      poblacion: json['population'] ,
      moneda   : json['currencies'],
      area     : json['area'],
      gini     : json['gini'],
      latlng   : json['latlng'],
      bandera  : json['flag'],
    );
  }

  String get getName              =>  this.name;
  String get getCapital           =>  this.capital;
  int get getPolacion             =>  this.poblacion;
  List<dynamic> get getMoneda     =>  this.moneda;
  double get getArea              =>  this.area;
  double get getGini              =>  this.gini;
  List<dynamic> get getLatlng     =>  this.latlng;
  String get getBandera           =>  this.bandera;
}

class ListaPaises extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => ListaPaisesState();
}

class ListaPaisesState extends State<ListaPaises> {
  List<Pais> data;
  @override
  Widget build(BuildContext context) {
    return Material(
      child:
      FutureBuilder<List<Pais>>(
        future: _fetch(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            data = snapshot.data;
            return _ListViewPais();
          } else if (snapshot.hasError) {
            return Text("${snapshot.error}");
          }
          return CircularProgressIndicator();
        },
      )
    );
  }

  Future<List<Pais>> _fetch() async {
    final response = await http.get('https://restcountries.eu/rest/v2/all');

    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((job) => new Pais.fromJson(job)).toList();
    } else {
      throw Exception('Failed to load jobs from API');
    }
  }

  Widget _ListViewPais() {
    return ListView.builder(
      itemCount: data.length,
        itemBuilder: (context, index) => GestureDetector(
            child: _tile(index, data[index]),
        )
    );
  }

  ListTile _tile(int index, Pais pais) => ListTile(
    title: FlatButton(
      onPressed: () => {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => DetailScreen(pais: data[index]),
          ),
        )
      },
      child: Text(
          pais.name,
          style: TextStyle(
            fontWeight: FontWeight.w500,
            fontSize: 20,
          )
      ),
    ),
  );
}

