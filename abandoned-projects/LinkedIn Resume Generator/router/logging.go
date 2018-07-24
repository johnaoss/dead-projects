package router

import (
	"log"
	"net/http"
)

// logging logs all requests.
func logging(h http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer log.Println(r.URL.String() + " " + r.Method)
		h.ServeHTTP(w, r)
	})
}
