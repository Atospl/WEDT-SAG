package parsers.dtos

import enums._

case class EntryDTO
(
  dateDownloaded: String,
  tags: Tag,
  siteFrom: Site,
  url: String,
  title: String
)
