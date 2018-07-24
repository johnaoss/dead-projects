package main

import (
	"fmt"
	"net/http"

	"github.com/johnaoss/yasp/router"
	"github.com/spf13/viper"
)

func main() {
	initConfig()
	clientID := viper.GetString("LinkedinID")
	clientSecret := viper.GetString("LinkedinSecret")
	if clientID == "" || clientSecret == "" {
		panic(fmt.Errorf("LinkedinID and LinkedinSecret must both be specified for this to work"))
	}
	port := ":" + viper.GetString("Port")
	credentials := &router.Credentials{ClientID: clientID, ClientSecret: clientSecret}
	context := router.AppContext{Creds: credentials}
	router := router.Init(context)
	fmt.Printf("Listening on %s\n", port)
	http.ListenAndServe(port, router)
}

func initConfig() {
	viper.SetConfigName("config")
	viper.AddConfigPath(".")
	viper.SetConfigType("yaml")
	err := viper.ReadInConfig()
	if err != nil {
		panic(fmt.Errorf("Couldn't load configuration file, please ensure config.yaml is in the same directory as main.go"))
	}
	viper.SetDefault("LinkedinID", "")
	viper.SetDefault("LinkedinSecret", "")
	viper.SetDefault("Port", 5000)
}
