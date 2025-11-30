# Hi Spy Android - Aplicativo Forense

Aplicativo Android para coleta forense de dados em dispositivos móveis.

## 🎯 Funcionalidades

- ✅ Coleta automática de contatos
- ✅ Coleta de mensagens SMS
- ✅ Coleta de registros de chamadas
- ✅ Rastreamento GPS em tempo real
- ✅ Envio automático de dados para servidor
- ✅ Execução em background sem interface

## 🔧 Compilação

### Pré-requisitos
- Android Studio Arctic Fox ou superior
- JDK 17
- Android SDK API 34

### Compilar APK

```bash
./gradlew assembleRelease
```

O APK será gerado em: `app/build/outputs/apk/release/app-release-unsigned.apk`

## 🚀 Instalação

```bash
adb install app/build/outputs/apk/release/app-release-unsigned.apk
```

## ⚠️ Aviso Legal

Este aplicativo é destinado **exclusivamente para fins acadêmicos e de pesquisa** na UFC Crateús.

**Uso restrito a dispositivos laboratoriais com consentimento explícito.**

## 📄 Licença

MIT License - Uso Acadêmico

## 👨‍🏫 Autor

Prof. Marcelo Claro - UFC Crateús