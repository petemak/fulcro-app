(ns app.mutations
   (:require [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
             [com.fulcrologic.fulcro.algorithms.merge :as merge]))


(defmutation delete-person-v1
  "delete persons with name from list with listname 'name' from the list with name 'list-mame' "
  [{:keys [id name list-name]}]
  
  (action [{:keys [state]}]
          (let [path (if (= "Friends" list-name)
                       [:friends :list/people]
                       [:enemies :list/people])
                old-list (get-in @state path)
                new-list (vec (filter #(not= (:person/id %) id) old-list))]
            (swap! state assoc-in path new-list ))))


(defmutation delete-person
  "Improved function that does not need global knowledge about structure of UI.
   Delete person with specied if from specified list id"
  [{list-id :list/id
    person-id :person/id}]
  (action [{:keys [state]}]
          (swap! state merge/remove-ident* [:person/id person-id] [:list/id list-id :list/people])))
