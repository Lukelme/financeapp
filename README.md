# 💰 FinanceApp — Controle Financeiro para Android

App Android nativo desenvolvido em **Kotlin** para gestão de finanças pessoais.

---

## 📱 Telas do App

| Início | Lançamentos | Relatórios | Metas |
|--------|-------------|------------|-------|
| Saldo, receitas e despesas | Lista completa de transações | Gráficos de pizza e barras | Metas de economia com progresso |

---

## ✨ Funcionalidades

- ✅ **Lançar receitas e despesas** com data, categoria e observação
- 📊 **Gráficos e relatórios** — pizza (receitas vs despesas) e barras (gastos mensais)
- 🏷️ **Categorias personalizadas** com emoji, cor e limite de orçamento
- 🎯 **Metas de economia** com barra de progresso e contribuições
- 🔔 **Notificações de limite** quando um categoria atinge 80% do orçamento
- 💾 **Banco de dados local** com Room (sem necessidade de internet)

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|------------|-----|
| Kotlin | Linguagem principal |
| MVVM | Arquitetura do projeto |
| Room | Banco de dados local |
| LiveData + ViewModel | Gerenciamento de estado |
| Navigation Component | Navegação entre telas |
| MPAndroidChart | Gráficos interativos |
| Material Design 3 | Interface visual |
| Coroutines | Operações assíncronas |

---

## 🚀 Como rodar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/FinanceApp.git
```

2. Abra no **Android Studio Hedgehog** (ou mais recente)

3. Aguarde o Gradle sincronizar as dependências

4. Rode em um emulador ou dispositivo com **Android 8.0+**

---

## 📁 Estrutura do Projeto

```
app/src/main/java/com/financeapp/
├── data/
│   ├── database/      # Room Database, DAOs
│   ├── models/        # Transaction, Category, Goal
│   └── repository/    # FinanceRepository
├── ui/
│   ├── home/          # Dashboard principal
│   ├── transactions/  # Lista e formulário de lançamentos
│   ├── reports/       # Gráficos e relatórios
│   └── goals/         # Metas de economia
├── viewmodel/         # FinanceViewModel (MVVM)
└── notifications/     # NotificationHelper
```

---

## 👨‍💻 Desenvolvedor

**Seu Nome** — [@seu-usuario](https://github.com/seu-usuario)

Desenvolvido como projeto de portfólio para demonstrar habilidades em desenvolvimento Android nativo com Kotlin.

---