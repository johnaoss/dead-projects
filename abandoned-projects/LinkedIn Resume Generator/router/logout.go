package router

import (
	"net/http"
)

// logoutHandler removes stored cookies and logs the user out.
func logoutHandler(c *AppContext, w http.ResponseWriter, r *http.Request) (int, error) {
	clearState(w, r)
	http.Redirect(w, r, "/", 303)
	return http.StatusOK, nil
}
