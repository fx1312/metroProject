// How to use : call one of these functions at the end of file
// Run file with following commande : $ node tools.js

var metro = require("./reseaux.json");

// Find details about a specific station, by name :
function stationInfo(name) {
  Object.keys(metro.stations).forEach(function(s) {
    if (metro.stations[s].nom === name) {
      console.log(metro.stations[s]);
    }
  });
}

function describeCorresp() {
  metro.corresp.forEach(function(correspondance) {
    console.log("Correspondance :");
    correspondance.forEach(function(s) {
      var station = metro.stations[s];
      console.log(station.nom + "(" + s + ")");
      console.log(station.lignes);
    })
    console.log("\n\n");
  })
}

function describeRoutes() {
  // List all connections :
  Object.keys(metro.routes).forEach(function(r) {
    console.log("Route :");
    // console.log(metro.routes[r].type);
    if (metro.routes[r].type == "corresp") {
      var arrets = metro.routes[r].arrets;

      var stations = arrets.map(function(arret) {
        return metro.stations[arret];
      });

      var stationsNames = stations.map(function(s) {
        return s.nom;
      });

      var stationsLines = stations.map(function(station) {
        var a = [];
        Object.keys(station.lignes).forEach(function(key) {
          station.lignes[key].forEach(function(ligne) {
            a.push("" + key + " " + ligne);
          })
        });
        return a;
        // return station.lignes.metro && station.lignes.metro.join(',');
      });

      console.log("Correspondance : " + stationsNames.join(", ") + " sur les lignes : " + stationsLines.join(', '))
    }
    console.log("\n\n");
  });
}

describeCorresp();
