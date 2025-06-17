package com.example.luqtaecommerce.presentation.main.products.model

enum class SortOption(val value: String?, val label: String) {
    Default(null, "افتراضي"),
    TitleAZ("name", "اسم المنتج (أ-ي)"),
    TitleZA("-name", "اسم المنتج (ي-أ)"),
    PriceAsc("price", "السعر تصاعدي"),
    PriceDesc("-price", "السعر تنازلي")
}