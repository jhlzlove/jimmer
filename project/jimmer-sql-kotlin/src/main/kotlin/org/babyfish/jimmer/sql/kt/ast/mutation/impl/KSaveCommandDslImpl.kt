package org.babyfish.jimmer.sql.kt.ast.mutation.impl

import org.babyfish.jimmer.sql.DeleteAction
import org.babyfish.jimmer.sql.ast.mutation.AbstractEntitySaveCommand
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.ast.mutation.KSaveCommandDsl
import org.babyfish.jimmer.sql.kt.toImmutableProp
import kotlin.reflect.KProperty1

internal class KSaveCommandDslImpl(
    private val javaCfg: AbstractEntitySaveCommand.Cfg
): KSaveCommandDsl {

    override fun setMode(mode: SaveMode) {
        javaCfg.setMode(mode)
    }

    override fun <E : Any> setKeyProps(vararg keyProps: KProperty1<E, *>) {
        javaCfg.setKeyProps(
            *keyProps.map { it.toImmutableProp() }.toTypedArray()
        )
    }

    override fun setAutoAttachingAll() {
        javaCfg.setAutoAttachingAll()
    }

    override fun setAutoAttaching(prop: KProperty1<*, *>) {
        javaCfg.setAutoAttaching(prop.toImmutableProp())
    }

    override fun setDeleteAction(prop: KProperty1<*, *>, action: DeleteAction) {
        javaCfg.setDeleteAction(prop.toImmutableProp(), action)
    }
}
