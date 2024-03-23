package common.dto.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("column1", "column2", "column3")
data class TestFileRow(
    @JsonProperty("column1")
    var column1: String?,
    @JsonProperty("column2")
    var column2: String?,
    @JsonProperty("column3")
    var column3: String?
)