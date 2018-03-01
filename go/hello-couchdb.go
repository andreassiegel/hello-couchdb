package main

import (
	"encoding/json"
	"fmt"
	"github.com/leesper/couchdb-golang"
	"log"
	"net/http"
	"os"
)

const (
	ServerPort = 9090
	CouchDbPort = 5986
	CouchDbHost = "localhost"
	CouchDbName = "people"
)

var (
	peopleDb *couchdb.Database
)

func main() {

	server := setUpCouchDbServer()
	databases, err := server.DBs()
	if err != nil {
		log.Fatal(err)
		os.Exit(1)
	}

	if !contains(databases, CouchDbName) {
		peopleDb = createDatabase(server, CouchDbName)
	} else {
		peopleDb = connectDatabase(server, CouchDbName)
	}

	http.HandleFunc("/people", list)
	http.HandleFunc("/people/add", add)

	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", ServerPort), nil))
}

func setUpCouchDbServer() *couchdb.Server {

	url := fmt.Sprintf("http://%s:%d", CouchDbHost, CouchDbPort)
	server, err := couchdb.NewServer(url)
	if err != nil {
		os.Exit(1)
	}
	return server
}

func contains(list []string, searchedValue string) bool {
	for _, value := range list {
		if value == searchedValue {
			return true
		}
	}
	return false
}

func createDatabase(server *couchdb.Server, dbName string) *couchdb.Database {
	fmt.Println(fmt.Sprintf("Creating database %s...", dbName))
	db, err := server.Create(CouchDbName)
	if err != nil {
		log.Fatal(err)
		os.Exit(1)
	}
	return db
}

func connectDatabase(server *couchdb.Server, dbName string) *couchdb.Database {

	fmt.Println(fmt.Sprintf("Connecting to database %s...", dbName))
	db, err := server.Get(CouchDbName)
	if err != nil {
		log.Fatal(err)
		os.Exit(1)
	}
	return db
}

func list(res http.ResponseWriter, req *http.Request) {

	ids, err := peopleDb.DocIDs()
	if err != nil {
		http.Error(res, err.Error(), http.StatusInternalServerError)
		return
	}

	var data []map[string]interface{}

	for _, id := range ids {
		doc, _ := peopleDb.Get(id, nil)
		data = append(data, doc)
	}

	res.Header().Set("Content-Type", "application/json")
	res.WriteHeader(http.StatusOK)
	json.NewEncoder(res).Encode(data)
}

func add(res http.ResponseWriter, req *http.Request) {

	decoder := json.NewDecoder(req.Body)
	var human map[string]interface{}
	err := decoder.Decode(&human)
	if err != nil {
		panic(err)
	}
	defer req.Body.Close()

	human["origin"] = "Go"

	id, _, err := peopleDb.Save(human, nil)
	if err != nil {
		panic(err)
	}

	doc, _ := peopleDb.Get(id, nil)

	res.Header().Set("Content-Type", "application/json")
	res.WriteHeader(http.StatusOK)
	json.NewEncoder(res).Encode(doc)
}
