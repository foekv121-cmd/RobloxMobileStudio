# Gemini AI (File > Gemini AI)

A chat helper for Luau and your game, using Google's Gemini API with **your own free API key**.

1. Get a key at https://aistudio.google.com/apikey (Google account needed).
2. File > Gemini AI > gear button: paste the key. Leave the model box empty; the app picks a working Gemini model itself.
3. Ask a question. Turn **Script: on** to include the Script / LocalScript / ModuleScript you have selected (first 12,000 characters).
4. **Insert code** puts the last ```lua block of the reply into a new Script named GeminiScript. **Copy** copies the whole reply.

Privacy and safety
- The key is stored only in this app's private storage, excluded from backups, never shown again (only the last 4 characters), and only sent to generativelanguage.googleapis.com. Plugins have no access to it.
- Nothing is sent until you tap Send. A notice appears the first time. On Google's free tier, Google may use what you send to improve its products, so don't send secrets.
- Replies are never run automatically. Read inserted code before you press Play.
- Free-tier rate limits apply; the app tells you when you hit one.

If it doesn't answer: after 45 seconds the app stops waiting and says so. Tap Stop to cancel earlier. In the gear menu, Test connection shows step by step whether the phone can find Google, reach it, and whether the key works.
