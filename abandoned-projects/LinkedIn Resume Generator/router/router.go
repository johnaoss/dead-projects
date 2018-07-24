package router

import (
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"golang.org/x/oauth2"
)

// ContextedHandler is a wrapper to provide AppContext to our Handlers
type ContextedHandler struct {
	*AppContext
	//ContextedHandlerFunc is the interface which our Handlers will implement
	ContextedHandlerFunc func(*AppContext, http.ResponseWriter, *http.Request) (int, error)
}

// AppContext provides shared info to handlers (i.e. Credentials)
type AppContext struct {
	Creds *Credentials
}

// Route defines a given route for the router.
type Route struct {
	Name             string
	Method           []string
	Pattern          string
	ContextedHandler *ContextedHandler
}

// These are placed here as they will never change and are needed by other files in this package.
var appContext AppContext
var authConf *oauth2.Config

// Routes contains all route information.
var routes = []Route{
	Route{
		"IndexHandler",
		[]string{"GET"},
		"/",
		&ContextedHandler{&appContext, indexHandler},
	},
	Route{
		"AuthHandler",
		[]string{"GET", "POST"},
		"/auth",
		&ContextedHandler{&appContext, authHandler},
	},
	Route{
		"LogoutHandler",
		[]string{},
		"/logout",
		&ContextedHandler{&appContext, logoutHandler},
	},
}

// ServeHTTP exists to satisfy the ServeHTTP request that Mux gives us as well as catching errors.
func (handler ContextedHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	status, err := handler.ContextedHandlerFunc(handler.AppContext, w, r)
	if err != nil {
		log.Printf("HTTP %d: %q", status, err)
		switch status {
		// TODO: Handle common errors.
		}
	}
}

// Init initializes the router, giving it the context specified in main.go
func Init(c AppContext) *mux.Router {
	router := mux.NewRouter()
	authConf = initAuth(c.Creds)
	appContext = c
	for _, r := range routes {
		router.Path(r.Pattern).Name(r.Name).Handler(logging(r.ContextedHandler))
	}
	return router
}
