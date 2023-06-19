package controleur.fin

import Main
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.stage.Stage
import kotlin.system.exitProcess

class ControleurBoutonMenu(private val appli: Main, private val stage: Stage) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {
        appli.relancerMenu(stage)
    }
}