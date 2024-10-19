package com.yoloroy.interpreter

data object NothingManageable : Manageable() {
    override val metatable: Map<String, ManageableFunction> = emptyMap()
}
