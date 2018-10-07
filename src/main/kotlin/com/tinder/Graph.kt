package com.tinder

sealed class Graph {
  sealed class State {
    object RegistrationValueProp : State()
    object WavyLoader : State()
    object ConfirmMailing : State()
    object InAddressFlow : State()
    object ConfirmData : State()
    object CustomerAgreement : State()
    object CustomerAgreementDenied : State()
    object CustomerAgreementLimbo : State()
    object DebitSuccess : State()
    object InIDVFlow : State()
    object PinSelectionLimbo : State()
    object InPinSetFlow : State()
    object VirtualCard : State()
    object CompletionSuccess : State()
    object PinSkipCompletionSuccess : State()

    override fun toString() = this.javaClass.simpleName
  }

  sealed class Event {
    object OnNext : Event()
    object OnSkip : Event()
    object OnBack : Event()
    object OnAction : Event()

    override fun toString() = this.javaClass.simpleName
  }

  sealed class SideEffect {
    object ShowState : SideEffect()
    object InLimbo : SideEffect()
    object GoBack : SideEffect()
    object FinishFailure : SideEffect()
    object FinishSuccess : SideEffect()

    override fun toString() = this.javaClass.simpleName
  }
}