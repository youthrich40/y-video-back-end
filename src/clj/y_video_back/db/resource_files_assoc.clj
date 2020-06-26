(ns y-video-back.db.resource-files-assoc
  (:require [y-video-back.db.core :as db]))

(def CREATE (partial db/CREATE :resource-files-assoc))
(def READ  (partial db/READ :resource-files-assoc-undeleted))
(def READ-ALL  (partial db/READ :resource-files-assoc))
(def UPDATE (partial db/UPDATE :resource-files-assoc))
(def DELETE (partial db/mark-deleted :resource-files-assoc))
(def CLONE (partial db/CLONE :resource-files-assoc))
(def PERMANENT-DELETE (partial db/DELETE :resource-files-assoc))
(def READ-BY-IDS "[column-vals & select-field-keys]\ncolumn-vals must be a resource containing resource-id then file-id. select-field-keys, if given, must be a resource containing keywords representing columns to return from db." (partial db/read-where-and :resource-files-assoc-undeleted [:resource-id :file-id]))
(def DELETE-BY-IDS "[column-vals]\ncolumn-vals must be a resource containing resource-id then file-id." (partial db/delete-where-and :resource-files-assoc-undeleted [:resource-id :file-id]))
(def READ-FILES-BY-CONTENT (partial db/read-all-where :files-by-resource :resource-id))
(def READ-CONTENTS-BY-FILE (partial db/read-all-where :resources-by-file :file-id))
(defn EXISTS-CONT-FILE? [resource-id file-id] (not (empty? (db/read-where-and :resource-files-assoc-undeleted [:resource-id :file-id] [resource-id file-id]))))
