# Security & privacy

BetterHabitsAI is a portfolio prototype; please do not treat it as a production-ready health or mental-health service.

## AI provider credentials

The original Android source archive included a **hard-coded provider API credential**. This public-source version contains **no provider key**, and the mobile client no longer adds an `Authorization: Bearer` header.

**If the original key is still valid, revoke/rotate it with the provider now.** Never commit the original ZIP, any APK built with that key, or screenshots showing credentials.

The AI features are **disabled by default** until you configure an HTTPS backend URL in `ApiClient.java`. Keep provider credentials only on the server. Implement authentication, authorization, usage limits and abuse monitoring for the backend. A public backend URL alone does not protect billable AI endpoints.

Do not embed secrets using Java source constants, XML resources, `BuildConfig`, or `local.properties`: values embedded in an Android APK can be recovered.

## Personal information

Journal entries, mood ratings and free-text reflections may contain sensitive information. The app stores records locally with Room; enabling AI functionality sends user-generated content to your server/provider. Before deploying to end users, implement consent, privacy disclosures, retention controls and a clear deletion path. Review Android backup settings as well.

## Reporting

Please do not post credentials or real personal journal data in public GitHub issues.
