package parsers.dtos

import java.sql.Timestamp

import enums._

case class EntryDTO
(
  dateDownloaded: String,
  tags: Tag,
  siteFrom: Site,
  url: String,
  title: String,
  publishedDate: Timestamp
)
