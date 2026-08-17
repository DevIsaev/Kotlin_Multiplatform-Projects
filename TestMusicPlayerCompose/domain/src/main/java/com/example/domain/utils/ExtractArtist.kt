package com.example.domain.utils

class ExtractArtist {

    operator fun invoke(title:String): Pair<String, String?>{

        var delimeter= if(title.contains(" - ")) " - "
        else if(title.contains("-")) "-"
        else null

        var artist=if(delimeter!=null) title.split(delimeter)[0] else null
        var titleWithoutArtist=title.replace("$artist$delimeter", "")

        return Pair(titleWithoutArtist,artist)
    }
}