package com.tinder

data class StateMachineModel(
    val initialState: String,
    val states: List<StateModel>
) {

  inline fun <reified T : Any> findClass(name: String): T {
    val className = T::class.java.name + "$$name"
    val clazz = Class.forName(className)
    val kclazz = clazz.kotlin
    return kclazz.objectInstance as T
  }

 inline fun <reified STATE : Any, reified EVENT : Any, reified SIDE_EFFECT : Any> toStateMachine(): StateMachine<STATE, EVENT, SIDE_EFFECT> {
    return StateMachine.create {
      initialState(findClass(initialState))
      states.forEach { stateModel ->
        state(findClass<STATE>(stateModel.name)) {
          stateModel.events.forEach { eventModel ->
            on(findClass<EVENT>(eventModel.name)) {
              if (eventModel.transition != null) {
                transitionTo(
                    findClass(eventModel.transition.state),
                    findClass(eventModel.transition.sideEffect)
                )
              } else {
                dontTransition(
                    findClass(eventModel.dontTransition!!.sideEffect)
                )
              }
            }
          }
        }
      }
    }
 }
}