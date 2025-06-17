# 👶 BabyCare

## 📝 Sobre o Projeto

O **BabyCare** é um aplicativo Android desenvolvido como parte do projeto acadêmico da disciplina de **Programação para Dispositivos Móveis (PDM)**.  
Seu objetivo principal é oferecer uma ferramenta simples e intuitiva para que pais e cuidadores possam monitorar e registrar os ciclos de sono e alimentação de seus bebês, gerando dados úteis para o acompanhamento da rotina da criança.

---

## 🎨 Design e Wireframe (Figma)

Toda a identidade visual e o fluxo de telas do aplicativo foram cuidadosamente planejados e prototipados no **Figma**.  
O design busca ser **amigável, acolhedor e funcional**, facilitando o uso mesmo em momentos de cansaço.

---

## ✨ Funcionalidades Planejadas

Abaixo estão os requisitos definidos para o projeto, com o status atual de implementação:

| Status | Funcionalidade                       | Tipo           |
|--------|--------------------------------------|----------------|
| ✅     | Cadastro de horário de sono          | Funcional      |
| ⏳     | Cadastro de alimentação              | Funcional      |
| ❌     | Relatório de sono (com gráficos)     | Funcional      |
| ❌     | Relatório de alimentação             | Funcional      |
| ✅     | Autenticação (Login e Senha)         | Não Funcional  |
| ✅     | Modo Noturno                         | Não Funcional  |
| ⏳     | Botão de Voltar                      | Não Funcional  |
| ✅     | Login com Google                     | Extra (+10%)   |
| ❌     | Arrasta pra trás para voltar         | Extra (+10%)   |
| ❌     | Tela de Splash e Wizard              | Extra          |

### 🗂️ Legenda:
- ✅ **Implementado**: A funcionalidade está presente no código.
- ⏳ **Em Progresso**: A funcionalidade está parcialmente implementada (ex: UI pronta, lógica pendente).
- ❌ **Não Iniciado**: A funcionalidade ainda não foi implementada.

---

## 🚀 Tecnologias Utilizadas

Este projeto foi construído utilizando tecnologias modernas do ecossistema Android:

| Tecnologia         | Descrição                                                                 |
|--------------------|---------------------------------------------------------------------------|
| **Kotlin**         | Linguagem de programação oficial para o desenvolvimento Android.          |
| **Android Nativo** | Desenvolvimento focado na plataforma Android para máxima performance.     |
| **SQLite**         | Banco de dados local para armazenamento persistente dos registros.        |
| **Firebase**       | Autenticação de usuários, incluindo Login com Google.                     |
| **Material Design 3** | Biblioteca de design para criar uma interface moderna e consistente.   |

---

## 💻 Status da Implementação

- **Banco de Dados (SQLite):**  
  A estrutura do banco de dados local com o `SQLiteOpenHelper` está criada. As tabelas `users`, `babies`, `feedings` e `sleep_sessions` estão prontas para armazenar os dados.

- **Autenticação:**  
  A tela de login (`activity_login.xml`) e a funcionalidade de **Login com Google** estão integradas e funcionais, utilizando o **Firebase Authentication**.

- **Registro de Sono:**  
  A interface para registrar o início e o fim do sono (`activity_sleep_register.xml`) está implementada.  
  A lógica para salvar os dados no banco ainda precisa ser conectada.

- **Temas (Modo Noturno):**  
  A configuração de temas para **modo claro e escuro** (`themes.xml`) já está presente, cumprindo o requisito.

---

## 👨‍💻 Autor

**Anthony Passos**
**Guilherme Magarao**
**Ígor Rocha**
**Luis Henrique**
