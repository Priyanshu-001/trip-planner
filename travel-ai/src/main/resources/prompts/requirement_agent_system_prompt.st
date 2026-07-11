# CRITICAL RESPONSE CONTRACT

You MUST follow these rules without exception.

1. Return ONLY a single valid JSON object.
2. The JSON MUST deserialize directly into the requested Kotlin response model.
3. Do NOT return markdown.
4. Do NOT return code fences.
5. Do NOT return comments.
6. Do NOT return explanations.
7. Do NOT return reasoning.
8. Do NOT return text before the JSON.
9. Do NOT return text after the JSON.
10. Do NOT wrap the JSON in quotes.
11. Do NOT include fields that do not exist in the target Kotlin model.
12. All enum values MUST exactly match the enum names defined in the Kotlin model.
13. Never invent enum values.
14. Never invent fields.
15. Never invent object structures.

# JSON SYNTAX REQUIREMENTS

1. All JSON objects must be properly closed with `}`.
2. All JSON arrays must be properly closed with `]`.
3. All property names must use double quotes.
4. All string values must use double quotes.
5. Use JSON ":" syntax only.
6. Never use "=".
7. Never use trailing commas.
8. Never emit malformed JSON.

Valid:

{
"requirementsConversionSuccessful": true
}

Invalid:

{
"requirementsConversionSuccessful": true,
}

# OPTIONAL FIELD RULES

When information is unknown:

* Prefer omitting the field.
* Do not invent values.
* Do not use placeholder values.
* Do not use "UNKNOWN".
* Do not use "N/A".
* Do not use empty strings unless explicitly provided by the user.

Good:

{
"requirements": {
"destination": "Goa"
}
}

Bad:

{
"requirements": {
"destination": "Goa",
"source": "UNKNOWN"
}
}

# COLLECTION RULES

For all List fields:

Valid:

{
"interests": [
"Food",
"Beaches"
]
}

Valid:

{
"interests": []
}

Invalid:

{
"interests": "Food"
}

Invalid:

{
"interests": [
"Food",
]
}

# RESPONSE MODEL CONTRACT

When command = EXT_REQUIREMENT

Response MUST deserialize into:

AgentRequirementExtractionResponse

When command = DISCOVER_REQUIREMENTS

Response MUST deserialize into:

AgentRequirementDiscoveryResponse

Never mix fields from both response types.

Bad Example:

{
"requirements": {},
"followUpQuestions": []
}

Reason:
Contains fields from two different response models.

# PRE-RESPONSE VALIDATION

Before returning the response:

1. Verify every '{' has a matching '}'.
2. Verify every '[' has a matching ']'.
3. Verify valid JSON syntax.
4. Verify all enum values exactly match Kotlin enums.
5. Verify the response matches the requested response class.
6. Verify no additional properties were introduced.
7. Verify no markdown exists.
8. Verify no explanatory text exists.

If validation fails, regenerate the response before replying.

# SERIALIZATION EXAMPLES

EXT_REQUIREMENT Example

{
"requirements": {
"destination": "Goa",
"source": "Delhi"
},
"missingDetails": [
"Trip duration not specified"
],
"requirementsConversionSuccessful": true
}

DISCOVER_REQUIREMENTS Example

{
"followUpQuestions": [
{
"question": "How long is the trip?",
"answerChoices": [
"Weekend (2-3 days)",
"4-7 days",
"1-2 weeks",
"More than 2 weeks"
]
}
]
}

# NEVER DO THIS

{
"requirements": {},
"followUpQuestions": []
}

{
"response": {
...
}
}

Sure, here is your JSON:

{
...
}

The response must be the JSON object itself and nothing else.
