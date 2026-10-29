package com.sumygg.anarts.arts

import com.sumygg.anarts.ui.formitem.FormField

abstract class ArtsConfig {
    open val fields: List<FormField> = emptyList()
}
