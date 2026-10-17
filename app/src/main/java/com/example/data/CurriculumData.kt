package com.example.data

import com.example.model.*

object CurriculumData {

    val DEEP_LEARNING_TOPIC = StudyTopic(
        id = "deep_learning_backprop",
        title = LocalizedText(
            en = "Deep Learning: Backpropagation & Optimization",
            te = "డీప్ లెర్నింగ్: బ్యాక్‌ప్రొపగేషన్ & ఆప్టిమైజేషన్",
            hi = "डीप लर्निंग: बैकप्रॉपैगैशन और अनुकूलन"
        ),
        field = "Computer Science & AI (GATE / M.Tech)",
        summary = LocalizedText(
            en = "Master the vector calculus of backpropagation, vanishing gradients, and gradient descent dynamics with live interactive simulations.",
            te = "లైవ్ ఇంటరాక్టివ్ సిమ్యులేషన్లతో బ్యాక్‌ప్రొపగేషన్, వెక్టర్ కాలిక్యులస్ మరియు గ్రేడియంట్ డీసెంట్ విధానాలను సులభంగా నేర్చుకోండి.",
            hi = "बैकप्रॉपैगैशन, वेक्टर कैलकुलस और ग्रेडिएंट डिसेंट डायनेमिक्स को लाइव इंटरैक्टिव सिमुलेशन के साथ समझें।"
        ),
        leadCharacterId = "vikram",
        slides = listOf(
            LectureSlide(
                id = "dl_s1",
                speakerId = "ananya",
                title = LocalizedText(
                    en = "The Intuition of Neural Loss Surfaces",
                    te = "న్యూరల్ లాస్ సర్ఫేస్ యొక్క ప్రాథమిక భావన",
                    hi = "न्यूरल लॉस सरफेस की सहज समझ"
                ),
                spokenDialogue = LocalizedText(
                    en = "Welcome scholars! Imagine standing on a foggy mountain peak trying to find the lowest valley. That valley is your global minimum error. In higher studies, we don't just guess; we use the gradient vector to find the steepest descent direction.",
                    te = "నమస్కారం విద్యార్థులారా! దట్టమైన పొగమంచులో పర్వతం నుండి లోయను ఎలా చేరుకోవాలో ఆలోచించండి. అదే మన గ్లోబల్ మినిమమ్ ఎర్రర్. ఉన్నత చదువుల్లో గ్రేడియంట్ వెక్టర్ సహాయంతో వేగవంతమైన మార్గాన్ని లెక్కిస్తాము.",
                    hi = "स्वागत है शोधार्थियों! कल्पना कीजिए कि आप कोहरे से ढके पहाड़ पर खड़े हैं और सबसे निचली घाटी ढूंढ रहे हैं। वही घाटी हमारा न्यूनतम एरर है। उच्च अध्ययन में हम ग्रेडिएंट वेक्टर द्वारा सबसे तेज गिरावट की दिशा ज्ञात करते हैं।"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Loss Function J(w, b) measures prediction error across batches.",
                        te = "• లాస్ ఫంక్షన్ J(w, b) ప్రిడిక్షన్ లోపాన్ని కొలుస్తుంది.",
                        hi = "• लॉस फंक्शन J(w, b) बैचों में भविष्यवाणी त्रुटि मापता है।"
                    ),
                    LocalizedText(
                        en = "• Weight update rule: W := W - η · ∇J(W)",
                        te = "• వెయిట్ అప్‌డేట్ సూత్రం: W := W - η · ∇J(W)",
                        hi = "• वेट अपडेट नियम: W := W - η · ∇J(W)"
                    ),
                    LocalizedText(
                        en = "• Learning rate (η) governs convergence stability.",
                        te = "• లెర్నింగ్ రేట్ (η) కన్వర్జెన్స్ స్థిరత్వాన్ని నిర్దేశిస్తుంది.",
                        hi = "• लर्निंग रेट (η) अभिसरण की स्थिरता को नियंत्रित करता है।"
                    )
                ),
                equationOrCode = "W_{t+1} = W_t - \\eta \\cdot \\frac{\\partial \\mathcal{L}}{\\partial W_t}"
            ),
            LectureSlide(
                id = "dl_s2",
                speakerId = "vikram",
                title = LocalizedText(
                    en = "The Chain Rule & Vanishing Gradient Trap",
                    te = "చైన్ రూల్ & వానిషింగ్ గ్రేడియంట్ సమస్య",
                    hi = "चेन रूल और वैनिशिंग ग्रेडिएंट जाल"
                ),
                spokenDialogue = LocalizedText(
                    en = "Pay close attention here. When we have 50 layers, repeated multiplication of small derivatives like sigmoid's maximum 0.25 drives earlier layer gradients to zero. This is why modern deep architectures depend on ReLU and residual connections.",
                    te = "ఇక్కడ చాలా శ్రద్ధ వహించండి. నెట్‌వర్క్‌లో 50 లేయర్లు ఉన్నప్పుడు, సిగ్మోయిడ్ డెరివేటివ్ (గరిష్టం 0.25) పదే పదే గుణించబడి ప్రారంభ లేయర్ల గ్రేడియంట్లను సున్నా చేస్తుంది. అందుకే ఆధునిక వ్యవస్థలు ReLU ను ఉపయోగిస్తాయి.",
                    hi = "यहाँ पूरा ध्यान दें। जब हमारे पास 50 परतें होती हैं, तो सिग्मॉइड के अधिकतम 0.25 जैसे छोटे डेरिवेटिव का बार-बार गुणा प्रारंभिक परतों के ग्रेडिएंट को शून्य कर देता है। इसीलिए आधुनिक नेटवर्क ReLU का उपयोग करते हैं।"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Chain Rule: ∂L/∂w1 = (∂L/∂y) · (∂y/∂h) · (∂h/∂w1)",
                        te = "• చైన్ రూల్: ∂L/∂w1 = (∂L/∂y) · (∂y/∂h) · (∂h/∂w1)",
                        hi = "• चेन नियम: ∂L/∂w1 = (∂L/∂y) · (∂y/∂h) · (∂h/∂w1)"
                    ),
                    LocalizedText(
                        en = "• Sigmoid derivative max is 0.25; (0.25)^10 ≈ 9.5e-7 (vanished!)",
                        te = "• సిగ్మోయిడ్ డెరివేటివ్ గరిష్టం 0.25; లోతైన లేయర్లలో గ్రేడియంట్ క్షీణిస్తుంది.",
                        hi = "• सिग्मॉइड का अधिकतम डेरिवेटिव 0.25 है; गहरी परतों में ग्रेडिएंट गायब हो जाता है।"
                    )
                ),
                checkpointQuestion = CheckpointQuestion(
                    id = "cp_dl_1",
                    speakerId = "vikram",
                    promptHeading = LocalizedText(
                        en = "Prof. Vikram's Checkpoint Quiz",
                        te = "ప్రొఫెసర్ విక్రమ్ చెక్‌పాయింట్ క్విజ్",
                        hi = "प्रो. विक्रम की चेकपॉइंट प्रश्नोत्तरी"
                    ),
                    questionText = LocalizedText(
                        en = "Why does the standard Rectified Linear Unit (ReLU) function alleviate the vanishing gradient problem in deep networks?",
                        te = "డీప్ న్యూరల్ నెట్‌వర్క్‌లలో ReLU ఫంక్షన్ వానిషింగ్ గ్రేడియంట్ సమస్యను ఎందుకు తగ్గిస్తుంది?",
                        hi = "डीप नेटवर्क में मानक ReLU फ़ंक्शन वैनिशिंग ग्रेडिएंट समस्या को क्यों कम करता है?"
                    ),
                    options = listOf(
                        LocalizedText(
                            en = "Its derivative is constantly 1.0 for all positive activations, preventing fractional degradation.",
                            te = "అన్ని పాజిటివ్ విలువలకు దీని డెరివేటివ్ స్థిరంగా 1.0 ఉంటుంది, తద్వారా గ్రేడియంట్ క్షీణించదు.",
                            hi = "सभी सकारात्मक मानों के लिए इसका डेरिवेटिव स्थिर रूप से 1.0 होता है, जिससे ग्रेडिएंट कम नहीं होता।"
                        ),
                        LocalizedText(
                            en = "It normalizes all hidden layer outputs strictly between -1.0 and +1.0.",
                            te = "ఇది అన్ని అవుట్‌పుట్‌లను ఖచ్చితంగా -1 మరియు +1 మధ్య సాధారణీకరిస్తుంది.",
                            hi = "यह सभी आउटपुट को -1 और +1 के बीच सामान्य करता है।"
                        ),
                        LocalizedText(
                            en = "It introduces exponential saturation at high activation magnitudes.",
                            te = "ఇది అధిక విలువల వద్ద ఎక్స్‌పోనెన్షియల్ సంతృప్తతను కలిగిస్తుంది.",
                            hi = "यह उच्च मानों पर घातीय संतृप्ति उत्पन्न करता है।"
                        ),
                        LocalizedText(
                            en = "It automatically sets the learning rate to zero for dead neurons.",
                            te = "ఇది నిష్క్రియ న్యూరాన్ల లెర్నింగ్ రేట్‌ను సున్నా చేస్తుంది.",
                            hi = "यह निष्क्रिय न्यूरॉन्स के लिए लर्निंग रेट को स्वतः शून्य कर देता है।"
                        )
                    ),
                    correctIndex = 0,
                    correctFeedback = LocalizedText(
                        en = "Brilliant insight! Because d/dx(max(0, x)) = 1 for x > 0, backpropagating gradients pass through without exponential decay.",
                        te = "చాలా అద్భుతం! x > 0 ఉన్నప్పుడు డెరివేటివ్ 1.0 కావడం వల్ల గ్రేడియంట్లు ఎలాంటి క్షీణత లేకుండా వెనుకకు ప్రసరిస్తాయి.",
                        hi = "उत्कृष्ट! क्योंकि x > 0 के लिए d/dx(max(0,x)) = 1 होता है, बैकप्रॉपैगेटेड ग्रेडिएंट बिना किसी क्षरण के गुजरते हैं।"
                    ),
                    wrongFeedback = LocalizedText(
                        en = "Not quite. Remember that vanishing gradient is caused by repeatedly multiplying fractions (< 1.0). ReLU's gradient is strictly 1.0 for positive inputs!",
                        te = "సరైనది కాదు. భిన్నాలను (<1.0) పదే పదే గుణించడం వల్ల గ్రేడియంట్ సున్నా అవుతుంది. ReLU లో పాజిటివ్ భాగంలో డెరివేటివ్ ఎల్లప్పుడూ 1.0 గా ఉంటుంది!",
                        hi = "सही नहीं है। याद रखें कि वैनिशिंग ग्रेडिएंट 1.0 से छोटी संख्याओं के गुणन के कारण होता है। ReLU में सकारात्मक इनपुट के लिए डेरिवेटिव 1.0 रहता है!"
                    )
                )
            ),
            LectureSlide(
                id = "dl_s3",
                speakerId = "arjun",
                title = LocalizedText(
                    en = "Live Simulation: Parameter Optimization",
                    te = "లైవ్ సిమ్యులేషన్: లెర్నింగ్ రేట్ & మొమెంటం",
                    hi = "लाइव सिमुलेशन: लर्निंग रेट और मोमेंटम"
                ),
                spokenDialogue = LocalizedText(
                    en = "Hey engineers, Master Arjun here! Theory is good, but let's test it live! Slide the Learning Rate up and down below. Notice how high values cause erratic oscillation while ideal values guide the loss down gracefully.",
                    te = "మిత్రులారా, మాస్టర్ అర్జున్ వచ్చాడు! సిద్ధాంతం మాత్రమే కాకుండా దీన్ని ప్రాక్టికల్ గా చూద్దాం. క్రింద ఉన్న స్లైడర్లతో లెర్నింగ్ రేట్ మార్చండి, లాస్ ఎలా తగ్గుతుందో ప్రత్యక్షంగా గమనించండి!",
                    hi = "नमस्ते इंजीनियर्स, मास्टर अर्जुन हाजिर है! चलिए इसे लाइव टेस्ट करते हैं। नीचे लर्निंग रेट को बदलें और देखें कि कैसे गलत मान ऑसिलेशन उत्पन्न करते हैं और सही मान लॉस को कम करते हैं।"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Try η = 0.05 vs η = 0.9 to observe divergence.",
                        te = "• విచలనం గమనించడానికి η = 0.05 మరియు η = 0.9 లను పరీక్షించండి.",
                        hi = "• विचलन देखने के लिए η = 0.05 और η = 0.9 का परीक्षण करें।"
                    ),
                    LocalizedText(
                        en = "• Momentum dampens oscillations along high-curvature ravines.",
                        te = "• మొమెంటం తీవ్రమైన కంపనాలను అరికడుతుంది.",
                        hi = "• मोमेंटम तेज उतार-चढ़ाव को शांत करता है।"
                    )
                ),
                liveExample = LiveExample(
                    id = "ex_dl_sim",
                    title = LocalizedText(
                        en = "Neural Gradient Descent Optimizer",
                        te = "న్యూరల్ గ్రేడియంట్ డీసెంట్ ఆప్టిమైజర్",
                        hi = "न्यूरल ग्रेडिएंट डिसेंट ऑप्टिमाइज़र"
                    ),
                    description = LocalizedText(
                        en = "Adjust Learning Rate (η) and Momentum (β) to watch real-time epoch loss and weight trajectories converge.",
                        te = "ఎపోక్ లాస్ మరియు వెయిట్ కన్వర్జెన్స్ లైవ్‌గా చూడటానికి లెర్నింగ్ రేట్ మరియు మొమెంటం మార్చండి.",
                        hi = "लॉस और वेट्स के कन्वर्जेंस को लाइव देखने के लिए लर्निंग रेट और मोमेंटम समायोजित करें।"
                    ),
                    simulationKind = SimulationKind.NEURAL_NETWORK,
                    parameter1Label = LocalizedText(
                        en = "Learning Rate (η)",
                        te = "లెర్నింగ్ రేట్ (η)",
                        hi = "लर्निंग रेट (η)"
                    ),
                    parameter1Default = 0.15f,
                    parameter1Min = 0.01f,
                    parameter1Max = 1.0f,
                    parameter2Label = LocalizedText(
                        en = "Momentum (β)",
                        te = "మొమెంటం (β)",
                        hi = "मोमेंटम (β)"
                    ),
                    parameter2Default = 0.9f,
                    parameter2Min = 0.0f,
                    parameter2Max = 0.99f,
                    formulaDisplay = "v_t = \\beta v_{t-1} + (1-\\beta)\\nabla J(w)"
                )
            ),
            LectureSlide(
                id = "dl_s4",
                speakerId = "vikram",
                title = LocalizedText(
                    en = "Summary & Exam Readiness",
                    te = "సారాంశం & పరీక్షా సంసిద్ధత",
                    hi = "सारांश और परीक्षा की तैयारी"
                ),
                spokenDialogue = LocalizedText(
                    en = "Excellent work. You have mastered the mathematical underpinnings. We are now ready for the Master's level Mock Examination. Remember: always verify the gradient dimension matches the parameter tensor dimension!",
                    te = "చాలా బాగుంది! గణిత సూత్రాలు మరియు ప్రాక్టికల్ విజువలైజేషన్ పూర్తి చేసుకున్నారు. ఇప్పుడు మాక్ టెస్ట్ రాయడానికి మీరు సిద్ధంగా ఉన్నారు. గ్రేడియంట్ కొలతలను ఎల్లప్పుడూ సరిచూసుకోండి!",
                    hi = "बहुत बढ़िया! आपने गणितीय आधार समझ लिया है। अब हम उच्च स्तरीय मॉक परीक्षा के लिए तैयार हैं। ध्यान रखें: ग्रेडिएंट का आयाम हमेशा पैरामीटर टेंसर से मेल खाना चाहिए!"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Matrix Dimension Rule: dim(∂L/∂W) = dim(W)",
                        te = "• మ్యాట్రిక్స్ డైమెన్షన్ నియమం: dim(∂L/∂W) = dim(W)",
                        hi = "• मैट्रिक्स आयाम नियम: dim(∂L/∂W) = dim(W)"
                    ),
                    LocalizedText(
                        en = "• Gate Trap: Forgetting bias derivative during multi-layer expansions.",
                        te = "• పరీక్షా పొరపాటు: మల్టీ-లేయర్ విస్తరణలో బయాస్ డెరివేటివ్‌ను మరిచిపోవడం.",
                        hi = "• सामान्य गलती: मल्टी-लेयर गणना में बायस डेरिवेटिव को भूल जाना।"
                    )
                )
            )
        ),
        mockTest = MockTest(
            id = "mock_dl_gate",
            title = LocalizedText(
                en = "Higher Studies Mock Test: Deep Learning Dynamics",
                te = "హయ్యర్ స్టడీస్ మాక్ టెస్ట్: డీప్ లెర్నింగ్ డైనమిక్స్",
                hi = "उच्च अध्ययन मॉक टेस्ट: डीप लर्निंग डायनेमिक्स"
            ),
            durationMinutes = 10,
            questions = listOf(
                MockQuestion(
                    id = "q_dl_1",
                    questionNumber = 1,
                    examTag = "GATE CS & AI",
                    questionText = LocalizedText(
                        en = "In a feedforward neural network with L layers using the Sigmoid activation σ(z) = 1/(1+e^(-z)), what is the theoretical maximum value of the derivative σ'(z)?",
                        te = "సిగ్మోయిడ్ యాక్టివేషన్ ఫంక్షన్ σ(z) = 1/(1+e^(-z)) ఉపయోగించే న్యూరల్ నెట్‌వర్క్‌లో, డెరివేటివ్ σ'(z) యొక్క గరిష్ట విలువ ఎంత?",
                        hi = "सिग्मॉइड सक्रियण फ़ंक्शन σ(z) = 1/(1+e^(-z)) का उपयोग करने वाले नेटवर्क में डेरिवेटिव σ'(z) का सैद्धांतिक अधिकतम मान क्या है?"
                    ),
                    options = listOf(
                        LocalizedText(en = "1.00 at z = 0", te = "z = 0 వద్ద 1.00", hi = "z = 0 पर 1.00"),
                        LocalizedText(en = "0.50 at z = 1", te = "z = 1 వద్ద 0.50", hi = "z = 1 पर 0.50"),
                        LocalizedText(en = "0.25 at z = 0", te = "z = 0 వద్ద 0.25", hi = "z = 0 पर 0.25"),
                        LocalizedText(en = "0.10 at z = ±∞", te = "z = ±∞ వద్ద 0.10", hi = "z = ±∞ पर 0.10")
                    ),
                    correctIndex = 2,
                    remedialExplanation = RemedialExplanation(
                        speakerId = "vikram",
                        whyCorrect = LocalizedText(
                            en = "The derivative of σ(z) is σ(z)(1 - σ(z)). At z = 0, σ(0) = 0.5. Therefore, the maximum derivative is 0.5 × (1 - 0.5) = 0.25.",
                            te = "సిగ్మోయిడ్ డెరివేటివ్ σ(z)(1 - σ(z)). z = 0 వద్ద σ(0) = 0.5 అవుతుంది. కాబట్టి గరిష్ట డెరివేటివ్ 0.5 × 0.5 = 0.25.",
                            hi = "सिग्मॉइड का डेरिवेटिव σ(z)(1 - σ(z)) होता है। z = 0 पर σ(0) = 0.5 होता है। इसलिए अधिकतम मान 0.5 × 0.5 = 0.25 है।"
                        ),
                        trapAnalysis = listOf(
                            LocalizedText(
                                en = "Trap: 1.00 is the maximum of the sigmoid function itself as z -> ∞, NOT its derivative!",
                                te = "పొరపాటు: 1.00 అనేది సిగ్మోయిడ్ ఫంక్షన్ యొక్క గరిష్ట పరిమితి మాత్రమే, డెరివేటివ్ కాదు!",
                                hi = "धोखा: 1.00 स्वयं सिग्मॉइड फ़ंक्शन का अधिकतम मान है, इसके डेरिवेटिव का नहीं!"
                            ),
                            LocalizedText(
                                en = "Trap: 0.50 is σ(0), which is the midpoint value, not the derivative product.",
                                te = "పొరపాటు: 0.50 అనేది σ(0) విలువ, డెరివేటివ్ లబ్దం కాదు.",
                                hi = "धोखा: 0.50 केवल σ(0) का मान है।"
                            ),
                            LocalizedText(
                                en = "Correct: Evaluated precisely from σ(z)(1 - σ(z)) yielding 0.25.",
                                te = "సరైనది: సూత్రం ప్రకారం 0.25 ఖచ్చితమైన సమాధానం.",
                                hi = "सही: सटीक गणना के अनुसार 0.25 सही उत्तर है।"
                            ),
                            LocalizedText(
                                en = "Trap: At z = ±∞, the derivative asymptotically approaches 0, not 0.10.",
                                te = "పొరపాటు: అనంతం వద్ద డెరివేటివ్ 0 కి చేరుకుంటుంది.",
                                hi = "धोखा: अनंत पर डेरिवेटिव 0 के करीब पहुंचता है।"
                            )
                        ),
                        coreIntuition = LocalizedText(
                            en = "Because 0.25 < 1, chaining derivatives over 10 layers multiplies (0.25)^10 ≈ 10^(-6), causing the vanishing gradient phenomenon.",
                            te = "0.25 అనేది 1 కన్నా తక్కువ కాబట్టి, 10 లేయర్లలో ఇది గుణించబడి గ్రేడియంట్ సున్నా అవుతుంది.",
                            hi = "चूँकि 0.25 < 1 है, 10 परतों में गुणा करने पर यह लगभग शून्य हो जाता है।"
                        ),
                        mnemonicOrExamTip = LocalizedText(
                            en = "Memory Tip: 'Quarter Cap' — Sigmoid's slope can never exceed one quarter (1/4 or 0.25)!",
                            te = "గుర్తుంచుకునే చిట్కా: 'క్వార్టర్ క్యాప్' — సిగ్మోయిడ్ వాలు ఎప్పుడూ పావువంతు (0.25) మించదు!",
                            hi = "याद रखने का सूत्र: 'क्वार्टर कैप' — सिग्मॉइड की ढलान कभी भी 0.25 (1/4) से अधिक नहीं हो सकती!"
                        )
                    )
                ),
                MockQuestion(
                    id = "q_dl_2",
                    questionNumber = 2,
                    examTag = "GATE CS / Research",
                    questionText = LocalizedText(
                        en = "What is the primary effect of incorporating an exponentially decaying moving average of past squared gradients in the RMSprop and Adam optimizers?",
                        te = "RMSprop మరియు Adam ఆప్టిమైజర్లలో గత వర్గీకృత గ్రేడియంట్ల మూవింగ్ యావరేజ్‌ను ఉపయోగించడం వల్ల కలిగే ముఖ్య ప్రయోజనం ఏమిటి?",
                        hi = "RMSprop और Adam ऑप्टिमाइज़र में पिछले चुकता ग्रेडिएंट्स के मूविंग एवरेज का उपयोग करने का मुख्य प्रभाव क्या है?"
                    ),
                    options = listOf(
                        LocalizedText(
                            en = "It adaptively scales the learning rate inversely proportional to recent gradient magnitudes per parameter.",
                            te = "ఇది ప్రతి పరామితికి ఇటీవలి గ్రేడియంట్ పరిమాణానికి విలోమానుపాతంలో లెర్నింగ్ రేట్‌ను సర్దుబాటు చేస్తుంది.",
                            hi = "यह प्रत्येक पैरामीटर के लिए हालिया ग्रेडिएंट परिमाण के व्युत्क्रमानुपाती लर्निंग रेट को अनुकूलित करता है।"
                        ),
                        LocalizedText(
                            en = "It guarantees that the global minimum will always be reached in strictly non-convex landscapes.",
                            te = "నాన్-కాన్వెక్స్ ఉపరితలాలలో గ్లోబల్ మినిమమ్ చేరుకోవడానికి ఖచ్చితమైన హామీ ఇస్తుంది.",
                            hi = "यह गैर-उत्तल सतहों में हमेशा न्यूनतम प्राप्त करने की गारंटी देता है।"
                        ),
                        LocalizedText(
                            en = "It eliminates the need for computing Jacobians during the backward pass.",
                            te = "ఇది బ్యాక్‌వర్డ్ పాస్‌లో జాకోబియన్లను లెక్కించాల్సిన అవసరాన్ని తొలగిస్తుంది.",
                            hi = "यह बैकवर्ड पास के दौरान जैकोबियन की गणना करने की आवश्यकता को समाप्त करता है।"
                        ),
                        LocalizedText(
                            en = "It forces all hidden layer weights to sum to 1.0 at every epoch.",
                            te = "ఇది ప్రతి ఎపోక్ వద్ద వెయిట్ల మొత్తం 1.0 అయ్యేలా చేస్తుంది.",
                            hi = "यह प्रत्येक चक्र में सभी भारों का योग 1.0 करने के लिए बाध्य करता है।"
                        )
                    ),
                    correctIndex = 0,
                    remedialExplanation = RemedialExplanation(
                        speakerId = "arjun",
                        whyCorrect = LocalizedText(
                            en = "Dividing by the square root of past squared gradients normalizes step sizes: steep directions take smaller cautious steps, while shallow directions accelerate.",
                            te = "గత గ్రేడియంట్ల వర్గమూలంతో భాగించడం ద్వారా స్టెప్ సైజ్ స్థిరీకరించబడుతుంది. తీవ్రమైన వాలులలో నెమ్మదిగాను, తక్కువ వాలులలో వేగంగాను ప్రయాణిస్తుంది.",
                            hi = "पिछले वर्ग ग्रेडिएंट के वर्गमूल से विभाजित करने से कदम का आकार सामान्य हो जाता है: खड़ी दिशाओं में छोटे कदम और सपाट दिशाओं में तेज कदम।"
                        ),
                        trapAnalysis = listOf(
                            LocalizedText(
                                en = "Correct: Per-parameter adaptive step size is the defining hallmark of RMSprop/Adam.",
                                te = "సరైనది: పారామీటర్ల వారీగా లెర్నింగ్ రేట్ నియంత్రణ దీని ముఖ్య లక్షణం.",
                                hi = "सही: प्रत्येक पैरामीटर के लिए अनुकूलित दर RMSprop/Adam की पहचान है।"
                            ),
                            LocalizedText(
                                en = "Trap: No first-order gradient descent method can guarantee global minimum in arbitrary non-convex objectives (NP-hard).",
                                te = "పొరపాటు: సంక్లిష్ట నాన్-కాన్వెక్స్ సందర్భాల్లో గ్లోబల్ మినిమమ్‌కు ఎలాంటి హామీ ఉండదు.",
                                hi = "धोखा: गैर-उत्तल समस्याओं में कोई भी पहला ऑर्डर तरीका वैश्विक न्यूनतम की गारंटी नहीं दे सकता।"
                            ),
                            LocalizedText(
                                en = "Trap: Jacobians/gradients are still fundamentally required for backpropagation.",
                                te = "పొరపాటు: గ్రేడియంట్లను తప్పనిసరిగా లెక్కించాల్సిందే.",
                                hi = "धोखा: ग्रेडिएंट की गणना अभी भी अनिवार्य है।"
                            ),
                            LocalizedText(
                                en = "Trap: Softmax normalizes probabilities to 1.0, not weight tensors.",
                                te = "పొరపాటు: సాఫ్ట్‌మ్యాక్స్ మాత్రమే సంభావ్యతలను 1.0 చేస్తుంది, వెయిట్లను కాదు.",
                                hi = "धोखा: सॉफ्टमैक्स संभावनाओं को 1.0 करता है, भारों को नहीं।"
                            )
                        ),
                        coreIntuition = LocalizedText(
                            en = "Think of it as adaptive shock absorbers in a car traversing rocky terrain.",
                            te = "గతుకుల రోడ్డుపై కారులోని షాక్ అబ్సార్బర్లు ఎలా పనిచేస్తాయో అలాగే ఇది కుదుపులను నివారిస్తుంది.",
                            hi = "उबड़-खाबड़ सड़क पर चलने वाली कार के शॉक एब्जॉर्बर की तरह यह झटकों को नियंत्रित करता है।"
                        ),
                        mnemonicOrExamTip = LocalizedText(
                            en = "Exam Rule: 'Divide by RMS = Tame the Wild Gradients!'",
                            te = "పరీక్షా సూత్రం: 'RMS భాగహారం = తీవ్రమైన గ్రేడియంట్లకు బ్రేక్!'",
                            hi = "परीक्षा सूत्र: 'RMS से भाग = बेलगाम ग्रेडिएंट पर नियंत्रण!'"
                        )
                    )
                )
            )
        )
    )

    val PHARMA_TOPIC = StudyTopic(
        id = "pharma_clearance",
        title = LocalizedText(
            en = "Pharmacokinetics: Two-Compartment Model & Clearance",
            te = "ఫార్మకోకైనటిక్స్: క్లియరెన్స్ & డ్రగ్ ఎలిమినేషన్",
            hi = "फार्माकोकाइनेटिक्स: दो-कम्पार्टमेंट मॉडल और क्लीयरेंस"
        ),
        field = "Medical & Pharmaceutical Sciences (USMLE / GPAT)",
        summary = LocalizedText(
            en = "Understand steady-state drug concentration, Volume of Distribution (Vd), and renal vs hepatic clearance kinetics.",
            te = "ఔషధాల వ్యాప్తి, హాఫ్ లైఫ్ మరియు క్లియరెన్స్ కైనటిక్స్ పై ఉన్నత స్థాయి వైద్య శాస్త్ర అధ్యయనం.",
            hi = "दवा की सांद्रता, वितरण की मात्रा (Vd), और रीनल बनाम हेपेटिक क्लीयरेंस का गहन अध्ययन।"
        ),
        leadCharacterId = "ananya",
        slides = listOf(
            LectureSlide(
                id = "pharma_s1",
                speakerId = "ananya",
                title = LocalizedText(
                    en = "Volume of Distribution (Vd) & Plasma Concentration",
                    te = "వాల్యూమ్ ఆఫ్ డిస్ట్రిబ్యూషన్ (Vd) & ప్లాస్మా గాఢత",
                    hi = "वितरण की मात्रा (Vd) और प्लाज्मा सांद्रता"
                ),
                spokenDialogue = LocalizedText(
                    en = "Greetings future doctors! When you administer a medication intravenously, where does it go? The Apparent Volume of Distribution tells us whether the drug stays inside vascular plasma or diffuses deeply into adipose tissue.",
                    te = "వైద్య విద్యార్థులకు స్వాగతం! ఒక ఔషధాన్ని శరీరంలోకి ప్రవేశపెట్టినప్పుడు, అది రక్తం లోపలే ఉంటుందా లేదా కణజాలాలలోకి చేరుతుందా అనేది ఈ Vd సూచిస్తుంది.",
                    hi = "भावी डॉक्टरों का स्वागत है! जब एक दवा नसों में दी जाती है, तो क्या वह प्लाज्मा में रहती है या ऊतकों में गहराई तक फैलती है? Vd हमें यही बताता है।"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Vd = Dose / C0 (Initial Plasma Concentration).",
                        te = "• Vd = మోతాదు / C0 (ప్రారంభ ప్లాస్మా గాఢత).",
                        hi = "• Vd = खुराक / C0 (प्रारंभिक प्लाज्मा सांद्रता)।"
                    ),
                    LocalizedText(
                        en = "• High lipophilicity drugs (e.g. Amiodarone) exhibit Vd > 400 L.",
                        te = "• కొవ్వులో కరిగే మందుల Vd చాలా ఎక్కువగా (400 లీటర్ల పైగా) ఉంటుంది.",
                        hi = "• अत्यधिक लिपोफिलिक दवाओं का Vd बहुत अधिक (400L से अधिक) होता है।"
                    )
                ),
                equationOrCode = "V_d = \\frac{\\text{Dose}}{C_0}, \\quad t_{1/2} = \\frac{0.693 \\times V_d}{\\text{Clearance}}"
            ),
            LectureSlide(
                id = "pharma_s2",
                speakerId = "arjun",
                title = LocalizedText(
                    en = "Live Simulation: Two-Compartment Clearance",
                    te = "లైవ్ సిమ్యులేషన్: డ్రగ్ ఎలిమినేషన్ కర్వ్",
                    hi = "लाइव सिमुलेशन: ड्रग एलिमिनेशन वक्र"
                ),
                spokenDialogue = LocalizedText(
                    en = "Master Arjun here! Look at the clearance curve on your screen. The steep initial alpha phase is distribution, while the gentle beta phase is true hepatic/renal elimination. Move the clearance slider to see how organ failure delays excretion!",
                    te = "మాస్టర్ అర్జున్ మీతో! గ్రాఫ్ చూడండి, ప్రారంభంలో వేగంగా తగ్గడం డిస్ట్రిబ్యూషన్, ఆ తర్వాత నిదానంగా తగ్గడం మూత్రపిండాలు లేదా కాలేయం ద్వారా తొలగింపును చూపుతుంది.",
                    hi = "मास्टर अर्जुन यहाँ है! स्क्रीन पर क्लीयरेंस वक्र देखें। प्रारंभिक तीव्र ढलान वितरण है, जबकि बाद का धीमा ढलान उत्सर्जन है। स्लाइडर चलाकर देखें कि अंग विफलता कैसे इसे प्रभावित करती है!"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "• Clearance (CL) = Rate of elimination / Plasma concentration.",
                        te = "• క్లియరెన్స్ (CL) = ఎలిమినేషన్ రేటు / ప్లాస్మా గాఢత.",
                        hi = "• क्लीयरेंस (CL) = उत्सर्जन की दर / प्लाज्मा सांद्रता।"
                    ),
                    LocalizedText(
                        en = "• Half-life (t1/2) is directly proportional to Vd and inversely to CL.",
                        te = "• హాఫ్ లైఫ్ అనేది Vd కి అనులోమానుపాతంలోను, క్లియరెన్స్ కి విలోమానుపాతంలోను ఉంటుంది.",
                        hi = "• हाफ-लाइफ Vd के सीधे आनुपातिक और क्लीयरेंस के व्युत्क्रमानुपाती होती है।"
                    )
                ),
                liveExample = LiveExample(
                    id = "ex_pharma_sim",
                    title = LocalizedText(
                        en = "Drug Clearance & Half-Life Simulator",
                        te = "డ్రగ్ క్లియరెన్స్ & హాఫ్-లైఫ్ సిమ్యులేటర్",
                        hi = "ड्रग क्लीयरेंस और हाफ-लाइफ सिम्युलेटर"
                    ),
                    description = LocalizedText(
                        en = "Adjust Drug Clearance (L/hr) and Apparent Volume (L) to observe plasma decay curve and time to reach safe steady state.",
                        te = "ప్లాస్మాలో ఔషధం తగ్గే తీరును చూడటానికి క్లియరెన్స్ మరియు వాల్యూమ్ ఆఫ్ డిస్ట్రిబ్యూషన్ లను మార్చండి.",
                        hi = "प्लाज्मा सांद्रता क्षय वक्र और सुरक्षित स्थिति तक पहुंचने के समय का निरीक्षण करने के लिए क्लीयरेंस समायोजित करें।"
                    ),
                    simulationKind = SimulationKind.PHARMA_CLEARANCE,
                    parameter1Label = LocalizedText(
                        en = "Clearance CL (L/h)",
                        te = "క్లియరెన్స్ రేటు (L/h)",
                        hi = "क्लीयरेंस दर (L/h)"
                    ),
                    parameter1Default = 3.5f,
                    parameter1Min = 0.5f,
                    parameter1Max = 10.0f,
                    parameter2Label = LocalizedText(
                        en = "Volume of Dist. Vd (L)",
                        te = "వాల్యూమ్ Vd (L)",
                        hi = "वितरण आयतन Vd (L)"
                    ),
                    parameter2Default = 45.0f,
                    parameter2Min = 10.0f,
                    parameter2Max = 150.0f,
                    formulaDisplay = "C(t) = C_0 \\cdot e^{-\\frac{\\text{CL}}{V_d} t}"
                )
            )
        ),
        mockTest = MockTest(
            id = "mock_pharma_usmle",
            title = LocalizedText(
                en = "USMLE / Higher Pharma Mock Exam",
                te = "USMLE ఉన్నత ఫార్మకాలజీ మాక్ పరీక్ష",
                hi = "USMLE उच्च फार्माकोलॉजी मॉक परीक्षा"
            ),
            durationMinutes = 8,
            questions = listOf(
                MockQuestion(
                    id = "q_pharma_1",
                    questionNumber = 1,
                    examTag = "USMLE Step 1 / GPAT",
                    questionText = LocalizedText(
                        en = "A patient is administered a constant IV infusion of a drug with a clearance of 2 L/hr and a Vd of 50 L. Approximately how many half-lives will it take to reach 93.75% of steady-state plasma concentration?",
                        te = "ఒక రోగికి 2 L/hr క్లియరెన్స్ మరియు 50 L Vd కలిగిన ఔషధం ఇవ్వబడింది. స్థిరమైన గాఢతలో 93.75% చేరుకోవడానికి ఎన్ని హాఫ్-లైఫ్‌లు పడుతుంది?",
                        hi = "एक मरीज को 2 L/hr क्लीयरेंस और 50 L Vd वाली दवा का IV इन्फ्यूजन दिया जाता है। स्थिर-अवस्था की सांद्रता का 93.75% तक पहुँचने में लगभग कितने हाफ-लाइफ लगेंगे?"
                    ),
                    options = listOf(
                        LocalizedText(en = "2 half-lives (75%)", te = "2 హాఫ్-లైఫ్‌లు (75%)", hi = "2 हाफ-लाइफ (75%)"),
                        LocalizedText(en = "3 half-lives (87.5%)", te = "3 హాఫ్-లైఫ్‌లు (87.5%)", hi = "3 हाफ-लाइफ (87.5%)"),
                        LocalizedText(en = "4 half-lives (93.75%)", te = "4 హాఫ్-లైఫ్‌లు (93.75%)", hi = "4 हाफ-लाइफ (93.75%)"),
                        LocalizedText(en = "7 half-lives (99%)", te = "7 హాఫ్-లైఫ్‌లు (99%)", hi = "7 हाफ-लाइफ (99%)")
                    ),
                    correctIndex = 2,
                    remedialExplanation = RemedialExplanation(
                        speakerId = "ananya",
                        whyCorrect = LocalizedText(
                            en = "Under first-order kinetics: 1 t1/2 = 50%, 2 t1/2 = 75%, 3 t1/2 = 87.5%, 4 t1/2 = 93.75%, and 5 t1/2 = 96.875%. Exactly 4 half-lives are required for 93.75%!",
                            te = "మొదటి ఆర్డర్ సూత్రం ప్రకారం: 1వ హాఫ్-లైఫ్ కు 50%, 2వ దానికి 75%, 3వ దానికి 87.5%, మరియు 4వ హాఫ్-లైఫ్ కు ఖచ్చితంగా 93.75% చేరుకుంటుంది!",
                            hi = "प्रथम-क्रम कैनेटीक्स के अनुसार: 1 हाफ-लाइफ = 50%, 2 = 75%, 3 = 87.5%, और 4 हाफ-लाइफ में ठीक 93.75% सांद्रता प्राप्त होती है!"
                        ),
                        trapAnalysis = listOf(
                            LocalizedText(
                                en = "Trap: 2 half-lives only reaches 1 - (1/2)^2 = 75%.",
                                te = "పొరపాటు: 2 హాఫ్-లైఫ్‌ల వద్ద 75% మాత్రమే సాధ్యమవుతుంది.",
                                hi = "धोखा: 2 हाफ-लाइफ में केवल 75% ही प्राप्त होता है।"
                            ),
                            LocalizedText(
                                en = "Trap: 3 half-lives reaches 87.5%. Common hurried arithmetic mistake.",
                                te = "పొరపాటు: 3 హాఫ్-లైఫ్‌ల వద్ద 87.5% మాత్రమే ఉంటుంది.",
                                hi = "धोखा: 3 हाफ-लाइफ में 87.5% ही होता है।"
                            ),
                            LocalizedText(
                                en = "Correct: 1 - (1/2)^4 = 15/16 = 0.9375 or 93.75%.",
                                te = "సరైనది: 1 - (1/16) = 15/16 = 93.75%. ఖచ్చితమైన లెక్క.",
                                hi = "सही: 1 - (1/16) = 93.75%। सटीक गणित।"
                            ),
                            LocalizedText(
                                en = "Trap: 7 half-lives exceeds 99% (virtually 100% elimination or saturation).",
                                te = "పొరపాటు: 7 హాఫ్-లైఫ్‌ల వద్ద 99% దాటుతుంది.",
                                hi = "धोखा: 7 हाఫ్-लाइफ में 99% से अधिक हो जाता है।"
                            )
                        ),
                        coreIntuition = LocalizedText(
                            en = "Each half-life closes half the remaining gap to 100%: 50 -> 75 -> 87.5 -> 93.75 -> 96.88.",
                            te = "ప్రతి హాఫ్-లైఫ్ మిగిలిన దూరంలో సగాన్ని పూర్తి చేస్తుంది.",
                            hi = "प्रत्येक हाफ-लाइफ शेष दूरी को आधा कर देती है।"
                        ),
                        mnemonicOrExamTip = LocalizedText(
                            en = "High-Yield Rule of Thumb: 4 to 5 half-lives is the clinical benchmark for steady-state washout or saturation.",
                            te = "క్లినికల్ గోల్డెన్ రూల్: 4 నుండి 5 హాఫ్-లైఫ్‌లు శరీరంలో ఔషధ స్థిరత్వానికి ప్రమాణం.",
                            hi = "गोल्डन रूल: 4 से 5 हाफ-लाइफ दवा की स्थिरता या पूर्ण निष्कासन के लिए नैदानिक मानक है।"
                        )
                    )
                )
            )
        )
    )

    val OS_CONCURRENCY_TOPIC = StudyTopic(
        id = "os_concurrency",
        title = LocalizedText(
            en = "Operating Systems: Semaphores & Deadlock Graphs",
            te = "ఆపరేటింగ్ సిస్టమ్స్: సెమాఫోర్స్ & డెడ్‌లాక్ గ్రాఫ్స్",
            hi = "ऑपरेटिंग सिस्टम: सेमाफोर और डेडलॉक ग्राफ़"
        ),
        field = "Systems Engineering (GATE CSE / MS Core)",
        summary = LocalizedText(
            en = "Demystify Coffman conditions, counting vs binary semaphores, circular wait prevention, and resource allocation graphs.",
            te = "డెడ్‌లాక్ నివారణ పద్ధతులు, సెమాఫోర్ కౌంటింగ్ మరియు రిసోర్స్ అలొకేషన్ గ్రాఫ్‌ల విశ్లేషణ.",
            hi = "कॉफमैन शर्तों, सेमाफोर और संसाधन आवंटन ग्राफ़ में डेडलॉक रोकथाम का संपूर्ण अध्ययन।"
        ),
        leadCharacterId = "arjun",
        slides = listOf(
            LectureSlide(
                id = "os_s1",
                speakerId = "arjun",
                title = LocalizedText(
                    en = "The 4 Coffman Conditions of Deadlock",
                    te = "డెడ్‌లాక్ ఏర్పడటానికి 4 కాఫ్‌మ్యాన్ పరిస్థితులు",
                    hi = "डेडलॉक की 4 कॉफमैन शर्तें"
                ),
                spokenDialogue = LocalizedText(
                    en = "Welcome engineers! A deadlock happens when processes halt waiting for resources held by each other. Remember: deadlock occurs if and only if ALL four Coffman conditions hold simultaneously: Mutual Exclusion, Hold & Wait, No Preemption, and Circular Wait!",
                    te = "ఇంజనీరింగ్ మిత్రులకు స్వాగతం! ఒక ప్రాసెస్ కోసం అవసరమైన రిసోర్స్‌ను మరొక ప్రాసెస్ పట్టుకుని వదలకుండా ఉన్నప్పుడు డెడ్‌లాక్ వస్తుంది. దీనికి 4 పరిస్థితులు కలిసి రావాలి: మ్యూచువల్ ఎక్స్‌క్లూజన్, హోల్డ్ & వెయిట్, నో ప్రిఎమ్ప్షన్, మరియు సర్క్యులర్ వెయిట్!",
                    hi = "नमस्ते इंजीनियर्स! जब प्रक्रियाएं एक-दूसरे द्वारा पकड़े गए संसाधनों की प्रतीक्षा में रुक जाती हैं, तो डेडलॉक होता है। इसके लिए सभी 4 शर्तें एक साथ होनी चाहिए।"
                ),
                whiteboardNotes = listOf(
                    LocalizedText(
                        en = "1. Mutual Exclusion: At least 1 non-shareable resource.",
                        te = "1. మ్యూచువల్ ఎక్స్‌క్లూజన్: ఒకేసారి ఒకరికి మాత్రమే అనుమతి.",
                        hi = "1. म्यूचुअल एक्सक्लूजन: एक समय में केवल एक प्रक्रिया को अनुमति।"
                    ),
                    LocalizedText(
                        en = "2. Hold and Wait: Process holding resources while requesting more.",
                        te = "2. హోల్డ్ & వెయిట్: ఒకదాన్ని పట్టుకుని మరొకదాని కోసం ఎదురుచూడటం.",
                        hi = "2. होल्ड एंड वेट: संसाधन रखते हुए और की प्रतीक्षा करना।"
                    ),
                    LocalizedText(
                        en = "3. No Preemption: Resources cannot be forcibly seized.",
                        te = "3. నో ప్రిఎమ్ప్షన్: బలవంతంగా లాక్కోలేము.",
                        hi = "3. नो प्रीएम्पशन: जबरन संसाधन नहीं छीना जा सकता।"
                    ),
                    LocalizedText(
                        en = "4. Circular Wait: P0 waits for P1, P1 waits for P0.",
                        te = "4. సర్క్యులర్ వెయిట్: చక్రంలా ఒకరిపై ఒకరు ఆధారపడటం.",
                        hi = "4. सर्कुलर वेट: प्रक्रियाओं की एक वृत्ताकार श्रृंखला।"
                    )
                ),
                equationOrCode = "P(wait): S.value-- ; if (S.value < 0) block();\nV(signal): S.value++ ; if (S.value <= 0) wakeup();"
            )
        ),
        mockTest = MockTest(
            id = "mock_os_gate",
            title = LocalizedText(
                en = "GATE Systems Architecture: Concurrency Mock",
                te = "గేట్ సిస్టమ్స్ ఆర్కిటెక్చర్: కాన్‌కరెన్సీ మాక్ టెస్ట్",
                hi = "गेट सिस्टम आर्किटेक्चर: समवर्ती मॉक टेस्ट"
            ),
            durationMinutes = 8,
            questions = listOf(
                MockQuestion(
                    id = "q_os_1",
                    questionNumber = 1,
                    examTag = "GATE CSE",
                    questionText = LocalizedText(
                        en = "A system has 4 processes and 12 units of the same resource. What is the maximum number of resource units each process can demand such that a deadlock is mathematically guaranteed never to occur?",
                        te = "ఒక సిస్టమ్‌లో 4 ప్రాసెస్‌లు మరియు 12 రిసోర్స్ యూనిట్లు ఉన్నాయి. డెడ్‌లాక్ ఎప్పటికీ రాకుండా ఉండాలంటే ప్రతి ప్రాసెస్ గరిష్టంగా ఎన్ని యూనిట్లను డిమాండ్ చేయవచ్చు?",
                        hi = "एक प्रणाली में 4 प्रक्रियाएं और एक ही संसाधन की 12 इकाइयां हैं। प्रत्येक प्रक्रिया अधिकतम कितनी इकाइयों की मांग कर सकती है जिससे कभी डेडलॉक न हो?"
                    ),
                    options = listOf(
                        LocalizedText(en = "3 units", te = "3 యూనిట్లు", hi = "3 इकाइयां"),
                        LocalizedText(en = "4 units", te = "4 యూనిట్లు", hi = "4 इकाइयां"),
                        LocalizedText(en = "5 units", te = "5 యూనిట్లు", hi = "5 इकाइयां"),
                        LocalizedText(en = "6 units", te = "6 యూనిట్లు", hi = "6 इकाइयां")
                    ),
                    correctIndex = 0,
                    remedialExplanation = RemedialExplanation(
                        speakerId = "arjun",
                        whyCorrect = LocalizedText(
                            en = "For N processes with peak demand m and R total resources, deadlock-free condition requires: R >= N * (m - 1) + 1. Here: 12 >= 4*(m - 1) + 1 -> 11 >= 4m - 4 -> 4m <= 15 -> m = 3.",
                            te = "సూత్రం ప్రకారం: R >= N * (m - 1) + 1. ఇక్కడ 12 >= 4*(m - 1) + 1 అవుతుంది. కాబట్టి గరిష్ట డిమాండ్ m = 3 యూనిట్లు.",
                            hi = "सूत्र के अनुसार: R >= N * (m - 1) + 1। यहाँ 12 >= 4*(m - 1) + 1, इसलिए m का मान 3 होगा।"
                        ),
                        trapAnalysis = listOf(
                            LocalizedText(
                                en = "Correct: If each demands 3, worst case each holds 2 (total 8), leaving 4 free to satisfy at least one process!",
                                te = "సరైనది: ప్రతి ఒక్కరు 3 కోరితే, వరస్ట్ కేసులో అందరూ 2 పట్టుకున్నా 4 మిగులుతాయి.",
                                hi = "सही: यदि प्रत्येक 3 मांगता है, तो सबसे खराब स्थिति में भी संसाधन बचेंगे।"
                            ),
                            LocalizedText(
                                en = "Trap: If m = 4, worst case each holds 3 (total 12 allocated), leaving 0 free -> DEADLOCK!",
                                te = "పొరపాటు: m = 4 అయితే, ప్రతి ఒక్కరూ 3 పట్టుకుంటే మొత్తం 12 అయిపోయి డెడ్‌లాక్ వస్తుంది!",
                                hi = "धोखा: यदि m = 4, तो सभी 12 संसाधन फंस जाएंगे और डेडलॉक हो जाएगा!"
                            ),
                            LocalizedText(
                                en = "Trap: 5 units easily exceeds capacity (4 x 4 = 16 needed).",
                                te = "పొరపాటు: 5 యూనిట్లతో తీవ్రమైన కొరత ఏర్పడుతుంది.",
                                hi = "धोखा: 5 इकाइयों में भारी कमी होगी।"
                            ),
                            LocalizedText(
                                en = "Trap: 6 units is far beyond safe boundary.",
                                te = "పొరపాటు: 6 యూనిట్లు పూర్తిగా అసురక్షితం.",
                                hi = "धोखा: 6 इकाइयां पूरी तरह से असुरक्षित हैं।"
                            )
                        ),
                        coreIntuition = LocalizedText(
                            en = "Pigeonhole principle: Ensure at least one process gets its full quota so it finishes and releases all resources.",
                            te = "కనీసం ఒక్క ప్రాసెస్ అయినా పూర్తయ్యేలా చూస్తే, అది విడుదల చేసే రిసోర్స్‌తో మిగిలినవి పూర్తవుతాయి.",
                            hi = "कबूतर का सिद्धांत: सुनिश्चित करें कि कम से कम एक प्रक्रिया पूरी हो जाए ताकि वह संसाधन मुक्त कर सके।"
                        ),
                        mnemonicOrExamTip = LocalizedText(
                            en = "Golden GATE Formula: Total Resources >= N * (Max - 1) + 1",
                            te = "గేట్ పరీక్షా గోల్డెన్ ఫార్ములా: మొత్తం రిసోర్సులు >= N * (గరిష్టం - 1) + 1",
                            hi = "गोल्डन गेट फॉर्मूला: कुल संसाधन >= N * (अधिकतम - 1) + 1"
                        )
                    )
                )
            )
        )
    )

    val allTopics: List<StudyTopic> = listOf(
        DEEP_LEARNING_TOPIC,
        PHARMA_TOPIC,
        OS_CONCURRENCY_TOPIC
    )

    fun getTopicById(id: String): StudyTopic =
        allTopics.find { it.id == id } ?: DEEP_LEARNING_TOPIC
}
