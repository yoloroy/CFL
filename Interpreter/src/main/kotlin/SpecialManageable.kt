package com.yoloroy.interpreter

class SpecialManageable(val onBind: (Manageable) -> Unit, val onResolve: () -> Manageable) : Manageable() {
    override val metatable = emptyMap<String, ManageableFunction>()
}
