(ns app.mutations
  (:require [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]))




(defmutation delete-person
  "delete persons with name from list with listname 'name' from the list with name 'list-mame' "
  [{:keys [id name list-name]}]

  
  (action [{:keys [state]}]
          (let [path (if (= "Friends" list-name)
                       [:friends :list/people]
                       [:enemies :list/people])
                old-list (get-in @state path)
                new-list (vec (filter #(not= (:person/id %) id) old-list))]
            (swap! state assoc-in path new-list ))))
