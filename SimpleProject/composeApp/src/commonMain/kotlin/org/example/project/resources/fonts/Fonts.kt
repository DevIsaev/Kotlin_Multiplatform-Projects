package org.example.project.resources.fonts

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.example.project.resources.Res
import org.example.project.resources.cimeropro
import org.example.project.resources.consolamono_bold
import org.example.project.resources.montserrat_bold
import org.example.project.resources.montserrat_extrabold
import org.example.project.resources.montserrat_italic
import org.example.project.resources.montserrat_light
import org.example.project.resources.montserrat_medium
import org.example.project.resources.montserrat_regular
import org.example.project.resources.montserrat_semibold
import org.example.project.resources.zapussans_medium
import org.example.project.resources.zapussans_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font


object Fonts {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun Montserrat(): FontFamily = FontFamily(
        Font(
            resource = Res.font.montserrat_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.montserrat_italic,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resource = Res.font.montserrat_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
        Font(Res.font.montserrat_light, FontWeight.Light),
        Font(Res.font.montserrat_regular, FontWeight.Normal),
        Font(Res.font.montserrat_medium, FontWeight.Medium),
        Font(Res.font.montserrat_semibold, FontWeight.SemiBold),
        Font(Res.font.montserrat_bold, FontWeight.Bold),
        Font(Res.font.montserrat_extrabold, FontWeight.ExtraBold),
    )

    @Composable
    fun Zapussans(): FontFamily= FontFamily(
        Font(
            resource = Res.font.zapussans_regular,
        ),
        Font(
            resource = Res.font.zapussans_medium,
        ),
    )
    @Composable
    fun cimeropro(): FontFamily= FontFamily(
        Font(
            resource = Res.font.cimeropro,
        ),
    )
    @Composable
    fun consolamono(): FontFamily= FontFamily(
        Font(
            resource = Res.font.consolamono_bold,
        ),
    )
}
