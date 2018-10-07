package com.tinder

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.Graph.State
import com.tinder.Graph.State.*
import com.tinder.Graph.Event
import com.tinder.Graph.SideEffect
import com.tinder.Graph.Event.*
import com.tinder.Graph.SideEffect.*


fun main(args: Array<String>) {

  val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()

  val stateMachine = StateMachine.create<State, Event, SideEffect> {
    initialState(RegistrationValueProp)
    state<RegistrationValueProp> {
      on(OnNext) {
        transitionTo(WavyLoader, ShowState)
      }
      on(OnSkip) {
        dontTransition(FinishFailure)
      }
      on(OnBack) {
        dontTransition(FinishFailure)
      }
    }
    state<WavyLoader> {
      on(OnNext) {
        transitionTo(ConfirmMailing, ShowState)
      }
    }
    state<ConfirmMailing> {
      on(OnNext) {
        transitionTo(CustomerAgreement, ShowState)
      }
      on(OnAction) {
        transitionTo(InAddressFlow, ShowState)
      }
      on(OnBack) {
        dontTransition(FinishFailure)
      }
    }
    state<InAddressFlow> {
      on(OnNext) {
        transitionTo(CustomerAgreement, ShowState)
      }
      on(OnSkip) {
        transitionTo(ConfirmMailing, ShowState)
      }
    }
    state<ConfirmData> {
      on(OnNext) {
        transitionTo(CustomerAgreement, ShowState)
      }
      on(OnBack) {
        transitionTo(ConfirmMailing, GoBack)
      }
    }
    state<CustomerAgreement> {
      on(OnNext) {
        transitionTo(CustomerAgreementLimbo, InLimbo)
      }
      on(OnSkip) {
        transitionTo(CustomerAgreementDenied, ShowState)
      }
      on(OnBack) {
        transitionTo(ConfirmData, GoBack)
      }
    }
    state<CustomerAgreementLimbo> {
      on(OnNext) {
        transitionTo(DebitSuccess, ShowState)
      }
      on(OnSkip) {
        transitionTo(InIDVFlow, ShowState)
      }
    }
    state<CustomerAgreementDenied> {
      on(OnNext) {
        transitionTo(CustomerAgreement, GoBack)
      }
      on(OnSkip) {
        dontTransition(FinishFailure)
      }
    }
    state<InIDVFlow> {
      on(OnNext) {
        transitionTo(DebitSuccess, ShowState)
      }
      on(OnSkip) {
        dontTransition(FinishFailure)
      }
    }
    state<DebitSuccess> {
      on(OnNext) {
        transitionTo(PinSelectionLimbo, InLimbo)
      }
      on(OnBack) {
        dontTransition(FinishFailure)
      }
    }
    state<PinSelectionLimbo> {
      on(OnNext) {
        transitionTo(InPinSetFlow, ShowState)
      }
      on(OnSkip) {
        transitionTo(VirtualCard, ShowState)
      }
    }
    state<InPinSetFlow> {
      on(OnNext) {
        transitionTo(VirtualCard, ShowState)
      }
      on(OnSkip) {
        transitionTo(PinSkipCompletionSuccess, ShowState)
      }
    }
    state<VirtualCard> {
      on(OnNext) {
        transitionTo(CompletionSuccess, ShowState)
      }
      on(OnBack) {
        transitionTo(DebitSuccess, ShowState)
      }
    }
    state<CompletionSuccess> {
      on(OnNext) {
        dontTransition(FinishSuccess)
      }
    }
    state<PinSkipCompletionSuccess> {
      on(OnNext) {
        dontTransition(FinishSuccess)
      }
    }

    onTransition {
      val validTransition = it as? StateMachine.Transition.Valid
      validTransition?.let {
        when (it.sideEffect) {
          ShowState -> println("pushFragment(${it.toState})")
          GoBack -> println("onBackPressed(to -> ${it.toState})")
          InLimbo -> println("in LimboState(${it.toState})")
          FinishFailure -> println("finishFlowWithFailure)")
          FinishSuccess -> println("finishFlowWithSuccess)")
        }
      }
    }
  }

  println(stateMachine.dotDiagram<State, Event>())

  val model = stateMachine.toModel<State, Event>()
  val json = moshi.adapter(StateMachineModel::class.java).toJson(model)

  println(json)

  val stateMachineModel: StateMachineModel? = moshi
      .adapter(StateMachineModel::class.java)
      .fromJson(json)

  println(stateMachineModel)

  val myStateMachine = stateMachineModel?.toStateMachine<State, Event, SideEffect>()

  println(myStateMachine?.dotDiagram<State, Event>())
}