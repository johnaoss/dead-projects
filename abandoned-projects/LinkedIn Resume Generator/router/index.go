package router

import (
	"html/template"
	"net/http"
)

// IndexHandler renders the homepage, and generates & stores state
func indexHandler(c *AppContext, w http.ResponseWriter, req *http.Request) (int, error) {
	tmpl, err := template.ParseFiles("public/html/index.html")
	if err != nil {
		w.Write([]byte("Error Code 500, could not load template."))
		return http.StatusInternalServerError, nil
	}
	state := generateState()
	session, err := store.Get(req, "yasp-resume")
	if err != nil {
		w.Write([]byte("Error Code 500, could not retrieve cookies"))
		return http.StatusInternalServerError, nil
	}
	session.Values["state"] = state
	defer session.Save(req, w)
	vars := map[string]interface{}{
		"loginURL": getLoginURL(state),
	}
	tmpl.Execute(w, vars)
	return http.StatusOK, nil
}
