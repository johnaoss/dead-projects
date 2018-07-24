package router

import (
	"net/http"
	"testing"
)

// Creates a dummy server to test with.
func PrepareTests() {
	creds := &Credentials{ClientID: "example", ClientSecret: "example"}
	context := AppContext{Creds: creds}
	router := Init(context)
	http.ListenAndServe(":5000", router)
}

func TestRoutes(t *testing.T) {
	go PrepareTests()
	// Checks to see if every handler returns a successful request.
	for _, route := range routes {
		path := "http://localhost:5000" + route.Pattern
		resp, err := http.Get(path)
		if err != nil {
			t.Errorf("Could not get PATH: %s", path)
		}
		defer resp.Body.Close()
	}

	// Will error
	erroneousRoutes := [2]string{"/error", "/404"}
	for _, route := range erroneousRoutes {
		path := "http://localhost:5000" + route
		resp, _ := http.Get(path)
		if resp.StatusCode != 404 {
			t.Errorf("No error HTTP code given: %s", path)
		}
		defer resp.Body.Close()
	}
}
