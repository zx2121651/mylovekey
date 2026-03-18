import React, { useState, useRef, useEffect, memo, useMemo } from 'react';
import { ArrowRight, ChevronLeft, Check, ChevronUp, X, User, Smartphone } from 'lucide-react';

// --- 全局真实数据 ---
const PERSONA_DATA = [
  {id: 1, icon: "🎁", text: "风流浪子", hot: true}, 
  {id: 2, icon: "🍭", text: "话题延伸", hot: false},
  {id: 3, icon: "🌱", text: "温柔体贴", hot: true}, 
  {id: 4, icon: "🙄", text: "怼一下", hot: true}, 
  {id: 5, icon: "🎠", text: "富婆の爱", hot: true},
  {id: 6, icon: "😎", text: "湘伢子", hot: false}, 
  {id: 7, icon: "🦁", text: "霸道总裁", hot: true},
  {id: 8, icon: "🏹", text: "深夜热聊", hot: false}, 
  {id: 9, icon: "🧥", text: "成熟稳重", hot: true},
  {id: 10, icon: "😋", text: "幽默有梗", hot: true}, 
  {id: 11, icon: "🤞", text: "安慰鼓励", hot: false},
  {id: 12, icon: "😆", text: "川渝男友", hot: false}
];

const GROUPS = [
  [PERSONA_DATA[0], PERSONA_DATA[1], PERSONA_DATA[2]],
  [PERSONA_DATA[3], PERSONA_DATA[4], PERSONA_DATA[5]],
  [PERSONA_DATA[6], PERSONA_DATA[7], PERSONA_DATA[8]],
  [PERSONA_DATA[9], PERSONA_DATA[10], PERSONA_DATA[11]],
];

// --- 动画与全局样式 ---
const AnimationStyles = () => (
  <style>{`
    * {
      font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Helvetica Neue", Helvetica, Arial, "Hiragino Sans GB", "Microsoft YaHei", sans-serif;
      -webkit-tap-highlight-color: transparent;
    }
    
    .num-font { font-family: "DIN Alternate", "DIN Condensed", "SF Pro Display", -apple-system, sans-serif; font-variant-numeric: tabular-nums; }

    @keyframes marquee-left { 0% { transform: translateX(0); } 100% { transform: translateX(-50%); } }
    @keyframes slide-up { 0% { transform: translateY(100%); opacity: 0; } 100% { transform: translateY(0); opacity: 1; } }
    @keyframes pop-in { 0% { transform: scale(0.9); opacity: 0; } 100% { transform: scale(1); opacity: 1; } }
    @keyframes click-hint { 0%, 100% { transform: translate(0, 0) scale(1); opacity: 0.8; } 50% { transform: translate(-5px, -5px) scale(0.9); opacity: 1; } }
    @keyframes fade-in { from { opacity: 0; } to { opacity: 1; } }
    @keyframes text-fade-out { from { opacity: 1; transform: translateY(0); } to { opacity: 0; transform: translateY(-10px); } }
    @keyframes text-fade-in { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
    @keyframes float-y { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-10px); } }
    @keyframes float-y-slow { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-5px); } }
    @keyframes pulse-soft { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.05); } }
    @keyframes star-flicker { 0%, 100% { opacity: 0.2; transform: scale(0.8); } 50% { opacity: 1; transform: scale(1.1); } }
    @keyframes keyboard-breathe { 0%, 100% { transform: translateY(0) scale(1); } 50% { transform: translateY(-4px) scale(1.02); } }
    @keyframes bubble-absorb { 0% { transform: translate(var(--startX), -240px) rotate(var(--startRot)) scale(0.5); opacity: 0; } 20% { transform: translate(calc(var(--startX) * 0.8), -120px) rotate(calc(var(--startRot) * 0.5)) scale(1.05); opacity: 1; } 60% { transform: translate(calc(var(--startX) * 0.2), -30px) rotate(2deg) scale(0.95); opacity: 1; } 80% { transform: translate(0, 0px) rotate(0deg) scale(0.8); opacity: 0.8; } 100% { transform: translate(0, 30px) rotate(0deg) scale(0); opacity: 0; } }
    @keyframes check-pop { 0% { transform: scale(0); opacity: 0; } 60% { transform: scale(1.2); opacity: 1; } 80% { transform: scale(0.95); opacity: 1; } 100% { transform: scale(1); opacity: 1; } }
    @keyframes firework-star { 0% { transform: translate(0, 0) scale(0) rotate(0deg); opacity: 1; } 50% { transform: translate(var(--tx), var(--ty)) scale(1.2) rotate(var(--rot)); opacity: 1; } 100% { transform: translate(calc(var(--tx) * 1.2), calc(var(--ty) * 1.2)) scale(0) rotate(calc(var(--rot) * 1.5)); opacity: 0; } }
    @keyframes firework-dot { 0% { transform: translate(0, 0) scale(0); opacity: 1; } 50% { transform: translate(var(--tx), var(--ty)) scale(1); opacity: 1; } 100% { transform: translate(calc(var(--tx) * 1.2), calc(var(--ty) * 1.2)) scale(0); opacity: 0; } }
    @keyframes hand-point-down { 0%, 100% { transform: translateY(0) scale(1); } 50% { transform: translateY(10px) scale(0.95); } }
    @keyframes hand-point-left { 0%, 100% { transform: translateX(0) scale(1); } 50% { transform: translateX(-10px) scale(0.95); } }
    @keyframes hand-point-right { 0%, 100% { transform: translateX(0) scale(1); } 50% { transform: translateX(10px) scale(0.95); } }
    @keyframes draw-arrow { from { stroke-dashoffset: 100; } to { stroke-dashoffset: 0; } }
    @keyframes toast-fade { 0% { opacity: 0; transform: translateY(10px); } 15% { opacity: 1; transform: translateY(0); } 85% { opacity: 1; transform: translateY(0); } 100% { opacity: 0; transform: translateY(-10px); } }
    @keyframes celebration-pop { 0% { transform: scale(0.5); opacity: 0; } 60% { transform: scale(1.1); opacity: 1; } 100% { transform: scale(1); opacity: 1; } }

    @keyframes grand-firework-particle {
      0% { transform: translate(0, 0) scale(0); opacity: 1; }
      40% { transform: translate(var(--tx), var(--ty)) scale(1); opacity: 1; }
      100% { transform: translate(calc(var(--tx) * 1.1), calc(var(--ty) * 1.1 + 40px)) scale(0); opacity: 0; }
    }
    @keyframes grand-firework-trail {
      0% { width: 0; opacity: 1; transform: translate(0,0) rotate(var(--rot)); }
      40% { width: var(--len); opacity: 1; transform: translate(var(--tx), var(--ty)) rotate(var(--rot)); }
      100% { width: 0; opacity: 0; transform: translate(calc(var(--tx)*1.1), calc(var(--ty)*1.1 + 20px)) rotate(var(--rot)); }
    }

    @keyframes heart-wave {
      0% { transform: translateX(0); }
      100% { transform: translateX(-100px); }
    }
    @keyframes heart-wave-reverse {
      0% { transform: translateX(-100px); }
      100% { transform: translateX(0); }
    }
    @keyframes heart-liquid-pulse {
      0%, 100% { transform: scaleY(1); }
      50% { transform: scaleY(1.05); }
    }
    @keyframes float-bubble {
      0% { transform: translateY(0) scale(0.5); opacity: 0; }
      30% { opacity: 0.7; transform: translateY(-10px) scale(1); }
      100% { transform: translateY(-30px) scale(0); opacity: 0; }
    }

    .text-stroke-black { -webkit-text-stroke: 1.5px #1A1A1A; color: white; text-shadow: 0 4px 8px rgba(0,0,0,0.15); }
    .text-stroke-blue { -webkit-text-stroke: 1px #4B66FF; }
    .text-stroke-red { -webkit-text-stroke: 1.5px #E02020; color: white; }
    .brush-text { font-family: sans-serif; font-style: italic; font-weight: 900; letter-spacing: 1px; color: white; -webkit-text-stroke: 1.5px white; }
    
    .animate-marquee-left { display: flex; width: max-content; animation: marquee-left linear infinite; }
    .animate-slide-up { animation: slide-up 0.4s cubic-bezier(0.16, 1, 0.3, 1) forwards; }
    .animate-pop-in { animation: pop-in 0.3s cubic-bezier(0.16, 1, 0.3, 1) forwards; }
    .animate-click-hint { animation: click-hint 1.5s ease-in-out infinite; }
    .animate-fade-in { animation: fade-in 0.3s ease-out forwards; }
    .animate-float-y { animation: float-y 3.5s ease-in-out infinite; }
    .animate-float-y-slow { animation: float-y-slow 5s ease-in-out infinite; }
    .animate-pulse-soft { animation: pulse-soft 2s ease-in-out infinite; }
    .animate-star { animation: star-flicker 2s ease-in-out infinite; }
    .no-scrollbar::-webkit-scrollbar { display: none; }
  `}</style>
);

const LocalBackdrop = ({ active, onClick }) => {
  if (!active) return null;
  return <div className="absolute inset-0 bg-black/40 z-[50] cursor-pointer transition-opacity duration-300" onClick={onClick}></div>;
};

// --- SVG Icons & Graphics ---
const getZodiac = (month, day) => {
  const dates = [20, 19, 21, 20, 21, 21, 23, 23, 23, 23, 22, 22];
  const signs = ["摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"];
  return day < dates[month - 1] ? signs[month - 1] : signs[month % 12];
};
const getAge = (year) => new Date().getFullYear() - year;

const MaleAvatar = () => (
  <svg viewBox="0 0 120 120" className="w-[85%] h-[85%]" fill="none">
    <path d="M 18 64 C 18 60, 24 58, 26 62 C 28 58, 34 60, 34 64 C 34 70, 26 74, 26 74 C 26 74, 18 70, 18 64 Z" fill="#F8C5CD"/>
    <path d="M 94 76 C 94 73, 98 71, 100 74 C 102 71, 106 73, 106 76 C 106 81, 100 84, 100 84 C 100 84, 94 81, 94 76 Z" fill="#F8C5CD"/>
    <path d="M 38 60 C 38 35, 82 35, 82 60 C 82 85, 68 95, 60 95 C 52 95, 38 85, 38 60 Z" fill="#B9C7FF" />
    <path d="M 33 48 C 30 28, 55 18, 72 25 C 88 30, 92 42, 82 48 C 76 52, 65 42, 55 45 C 45 48, 40 58, 33 48 Z" fill="#1A1A1A" />
    <circle cx="50" cy="58" r="2.5" fill="#1A1A1A" />
    <circle cx="70" cy="58" r="2.5" fill="#1A1A1A" />
    <path d="M 60 58 L 60 68 L 64 68" stroke="#1A1A1A" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" />
    <path d="M 50 76 C 54 83, 62 83, 66 76" stroke="#1A1A1A" strokeWidth="2.5" strokeLinecap="round" />
  </svg>
);

const FemaleAvatar = () => (
  <svg viewBox="0 0 120 120" className="w-[85%] h-[85%]" fill="none">
    <path d="M 46 55 C 46 35, 78 35, 82 55 C 86 75, 76 90, 60 90 C 46 90, 42 75, 46 55 Z" fill="#FFD5CF" />
    <circle cx="85" cy="56" r="18" fill="#1A1A1A" />
    <path d="M 52 35 C 45 35, 45 48, 45 55 C 45 60, 52 55, 55 50 C 65 38, 75 42, 80 48 C 85 54, 96 66, 96 50 C 96 35, 75 22, 52 35 Z" fill="#1A1A1A" />
    <path d="M 35 55 C 40 60, 48 58, 52 53" stroke="#1A1A1A" strokeWidth="3" strokeLinecap="round" />
    <path d="M 35 55 L 29 52" stroke="#1A1A1A" strokeWidth="3" strokeLinecap="round" />
    <path d="M 48 56 L 44 66 L 50 66" stroke="#1A1A1A" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" />
    <path d="M 48 76 C 52 80, 56 78, 58 74" stroke="#1A1A1A" strokeWidth="3" strokeLinecap="round" />
  </svg>
);

const CakeIcon = () => (
  <svg width="90" height="90" viewBox="0 0 100 100" fill="none">
    <rect x="25" y="70" width="50" height="8" rx="2" fill="#F8B2AA" />
    <rect x="20" y="78" width="60" height="6" rx="2" fill="#F8B2AA" />
    <rect x="28" y="52" width="44" height="18" rx="4" fill="#C6D3FC" />
    <path d="M 28 62 Q 33 56 38 62 T 48 62 T 58 62 T 68 62 L 72 62" fill="none" stroke="#1A1A1A" strokeWidth="2.5" strokeLinecap="round" />
    <rect x="47" y="34" width="6" height="18" fill="white" stroke="#1A1A1A" strokeWidth="2.5" rx="1" />
    <path d="M 50 14 C 50 14 44 24 44 28 C 44 32 56 32 56 28 C 56 24 50 14 50 14 Z" fill="white" stroke="#1A1A1A" strokeWidth="2.5" strokeLinejoin="round" />
  </svg>
);

const SparkleIcon = ({ className, style }) => (
  <svg viewBox="0 0 20 20" className={className} style={style} fill="none">
    <path d="M10 0 C10 5, 15 10, 20 10 C15 10, 10 15, 10 20 C10 15, 5 10, 0 10 C5 10, 10 5, 10 0 Z" fill="currentColor" />
  </svg>
);

const VideoKeyboardGraphic = () => (
  <svg width="104" height="72" viewBox="0 0 104 72" fill="none" className="drop-shadow-[0_8px_24px_rgba(120,138,244,0.3)]">
    <rect x="0" y="0" width="104" height="72" rx="20" fill="#788AF4" />
    <rect x="14" y="20" width="10" height="6" rx="3" fill="white" />
    <rect x="30" y="20" width="10" height="6" rx="3" fill="white" />
    <rect x="46" y="20" width="10" height="6" rx="3" fill="white" />
    <rect x="62" y="20" width="10" height="6" rx="3" fill="white" />
    <rect x="78" y="20" width="10" height="6" rx="3" fill="white" />
    <rect x="22" y="34" width="10" height="6" rx="3" fill="white" />
    <rect x="38" y="34" width="10" height="6" rx="3" fill="white" />
    <rect x="54" y="34" width="10" height="6" rx="3" fill="white" />
    <rect x="70" y="34" width="10" height="6" rx="3" fill="white" />
    <rect x="30" y="48" width="42" height="6" rx="3" fill="white" />
  </svg>
);

const Keyboard3DGraphic = () => (
  <svg width="140" height="100" viewBox="0 0 140 100" fill="none" className="drop-shadow-[0_16px_32px_rgba(92,115,255,0.4)]">
    <rect x="10" y="20" width="120" height="76" rx="22" fill="#3D50E6" />
    <rect x="10" y="16" width="120" height="76" rx="22" fill="#5C73FF" />
    <rect x="10" y="12" width="120" height="76" rx="22" fill="#7A8FFF" />
    
    <g fill="#4B60D6">
      <rect x="22" y="32" width="14" height="12" rx="4" />
      <rect x="42" y="32" width="14" height="12" rx="4" />
      <rect x="62" y="32" width="14" height="12" rx="4" />
      <rect x="82" y="32" width="14" height="12" rx="4" />
      <rect x="102" y="32" width="14" height="12" rx="4" />
    </g>
    <g fill="#FFFFFF">
      <rect x="22" y="28" width="14" height="12" rx="4" />
      <rect x="42" y="28" width="14" height="12" rx="4" />
      <rect x="62" y="28" width="14" height="12" rx="4" />
      <rect x="82" y="28" width="14" height="12" rx="4" />
      <rect x="102" y="28" width="14" height="12" rx="4" />
    </g>
    
    <g fill="#4B60D6">
      <rect x="32" y="52" width="14" height="12" rx="4" />
      <rect x="52" y="52" width="14" height="12" rx="4" />
      <rect x="72" y="52" width="14" height="12" rx="4" />
      <rect x="92" y="52" width="14" height="12" rx="4" />
    </g>
    <g fill="#FFFFFF">
      <rect x="32" y="48" width="14" height="12" rx="4" />
      <rect x="52" y="48" width="14" height="12" rx="4" />
      <rect x="72" y="48" width="14" height="12" rx="4" />
      <rect x="92" y="48" width="14" height="12" rx="4" />
    </g>
    
    <g fill="#4B60D6">
      <rect x="42" y="72" width="56" height="12" rx="4" />
    </g>
    <g fill="#FFFFFF">
      <rect x="42" y="68" width="56" height="12" rx="4" />
    </g>
  </svg>
);

const VideoCheckCircle = () => (
  <svg width="64" height="64" viewBox="0 0 72 72" fill="none" className="drop-shadow-[0_8px_16px_rgba(120,138,244,0.4)]">
    <circle cx="36" cy="36" r="36" fill="#788AF4" />
    <path d="M 22 36 L 32 46 L 50 26" stroke="white" strokeWidth="5.5" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const CheckCircle3DGraphic = () => (
  <svg width="80" height="80" viewBox="0 0 80 80" fill="none" className="drop-shadow-[0_12px_24px_rgba(120,138,244,0.5)]">
    <circle cx="40" cy="44" r="32" fill="#3D50E6" />
    <circle cx="40" cy="40" r="32" fill="#6E85FF" />
    <path d="M 26 40 L 36 50 L 56 28" stroke="#3D50E6" strokeWidth="6" strokeLinecap="round" strokeLinejoin="round" />
    <path d="M 26 38 L 36 48 L 56 26" stroke="white" strokeWidth="6" strokeLinecap="round" strokeLinejoin="round" />
  </svg>
);

const ParticleBurst = ({ delayOffset = 0 }) => {
  const particles = useMemo(() => {
    const p = [];
    const colors = ['#FF4B6B', '#FFD233', '#5C73FF', '#4ECDC4', '#A855F7', '#FF923D', '#FFFFFF', '#00E676'];

    // 生成圆形光点粒子
    for (let i = 0; i < 40; i++) {
      const angle = (Math.random() * Math.PI * 2);
      const distance = 80 + Math.random() * 160;
      p.push({
        type: 'dot',
        tx: `${Math.cos(angle) * distance}px`,
        ty: `${Math.sin(angle) * distance}px`,
        color: colors[Math.floor(Math.random() * colors.length)],
        size: 4 + Math.random() * 8,
        delay: Math.random() * 0.3 + delayOffset,
        duration: 0.8 + Math.random() * 0.6
      });
    }
    // 生成长条拖尾流星
    for (let i = 0; i < 20; i++) {
      const angle = (Math.random() * Math.PI * 2);
      const distance = 100 + Math.random() * 120;
      p.push({
        type: 'streamer',
        tx: `${Math.cos(angle) * distance}px`,
        ty: `${Math.sin(angle) * distance}px`,
        rot: `${angle}rad`,
        color: colors[Math.floor(Math.random() * colors.length)],
        length: 20 + Math.random() * 40,
        delay: Math.random() * 0.2 + delayOffset,
        duration: 0.6 + Math.random() * 0.4
      });
    }
    return p;
  }, [delayOffset]);

  return (
    <div className="absolute top-1/2 left-1/2 w-0 h-0 pointer-events-none z-[150]">
      {particles.map((p, i) => {
        if (p.type === 'dot') {
          return (
            <div
              key={i}
              className="absolute rounded-full"
              style={{
                width: p.size,
                height: p.size,
                backgroundColor: p.color,
                marginLeft: -p.size/2,
                marginTop: -p.size/2,
                '--tx': p.tx,
                '--ty': p.ty,
                animation: `grand-firework-particle ${p.duration}s cubic-bezier(0.25, 1, 0.3, 1) forwards ${p.delay}s`,
                opacity: 0,
                boxShadow: `0 0 ${p.size}px ${p.color}`
              }}
            />
          );
        } else {
          return (
            <div
              key={i}
              className="absolute rounded-full"
              style={{
                height: 3 + Math.random()*2,
                backgroundColor: p.color,
                transformOrigin: 'left center',
                '--tx': p.tx,
                '--ty': p.ty,
                '--len': `${p.length}px`,
                '--rot': p.rot,
                animation: `grand-firework-trail ${p.duration}s cubic-bezier(0.25, 1, 0.3, 1) forwards ${p.delay}s`,
                opacity: 0,
                boxShadow: `0 0 8px ${p.color}`
              }}
            />
          );
        }
      })}
    </div>
  );
};

const FireworkBurst = () => (
  <div className="absolute top-1/2 left-1/2 w-0 h-0 pointer-events-none z-[100]">
    {/* 中心主爆炸 */}
    <ParticleBurst delayOffset={0} />
    {/* 四周错开的乱放爆炸群 */}
    <div className="absolute" style={{ transform: 'translate(-100px, -80px) scale(0.7)' }}><ParticleBurst delayOffset={0.2} /></div>
    <div className="absolute" style={{ transform: 'translate(110px, -50px) scale(0.8)' }}><ParticleBurst delayOffset={0.4} /></div>
    <div className="absolute" style={{ transform: 'translate(-30px, 100px) scale(0.6)' }}><ParticleBurst delayOffset={0.5} /></div>
    <div className="absolute" style={{ transform: 'translate(80px, 90px) scale(0.7)' }}><ParticleBurst delayOffset={0.7} /></div>
  </div>
);

const Screenshot3DKeycap = () => (
  <svg width="180" height="180" viewBox="0 0 150 150" fill="none" className="drop-shadow-[0_15px_30px_rgba(30,50,200,0.3)]">
    <rect x="20" y="30" width="110" height="100" rx="32" fill="#3D50E6" />
    <rect x="20" y="24" width="110" height="96" rx="32" fill="#6B8BFF" />
    <rect x="32" y="36" width="86" height="76" rx="24" fill="#D9E2FF" />
    <rect x="32" y="28" width="86" height="76" rx="24" fill="#F8FAFF" />
    <path d="M 38 32 C 60 28 90 28 112 32 C 114 45 114 55 112 65 C 90 55 60 55 38 65 C 36 55 36 45 38 32 Z" fill="white" />
    <circle cx="62" cy="62" r="5.5" fill="#2C334A" />
    <circle cx="88" cy="62" r="5.5" fill="#2C334A" />
    <path d="M 66 75 C 70 82 80 82 84 75" stroke="#2C334A" strokeWidth="5.5" strokeLinecap="round" fill="none" />
  </svg>
);

const AppIconBlue = () => (
  <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
    <rect width="36" height="36" rx="10" fill="#4B66FF" />
    <rect x="6" y="8" width="24" height="18" rx="6" fill="#FFD233" />
    <circle cx="13" cy="15" r="1.5" fill="#1A1A1A" />
    <circle cx="23" cy="15" r="1.5" fill="#1A1A1A" />
    <path d="M 15 20 C 16 22 20 22 21 20" stroke="#1A1A1A" strokeWidth="1.5" strokeLinecap="round" />
    <rect x="8" y="28" width="4" height="2" rx="1" fill="white" opacity="0.8" />
    <rect x="14" y="28" width="4" height="2" rx="1" fill="white" opacity="0.8" />
    <rect x="20" y="28" width="4" height="2" rx="1" fill="white" opacity="0.8" />
    <rect x="26" y="28" width="2" height="2" rx="1" fill="white" opacity="0.8" />
  </svg>
);

const KeyIconBackspace = () => (<svg viewBox="0 0 24 24" width="14" height="14" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round"><path d="M21 4H8l-7 8 7 8h13a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2z"></path><line x1="18" y1="9" x2="12" y2="15"></line><line x1="12" y1="9" x2="18" y2="15"></line></svg>);
const KeyIconMic = () => (<svg viewBox="0 0 24 24" width="12" height="12" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round"><path d="M12 2a3 3 0 0 0-3 3v7a3 3 0 0 0 6 0V5a3 3 0 0 0-3-3Z"></path><path d="M19 10v2a7 7 0 0 1-14 0v-2"></path><line x1="12" y1="19" x2="12" y2="22"></line></svg>);
const KeyIconShift = () => (<svg viewBox="0 0 24 24" width="14" height="14" stroke="currentColor" strokeWidth="2" fill="none" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3v18M12 3l7 7M12 3l-7 7" /></svg>);

const HeartPercentBadge = ({ percent = "100%", active = false }) => (
  <div className={`h-[26px] px-2.5 rounded-full flex items-center justify-center gap-1 shrink-0 ml-auto cursor-pointer active:scale-95 transition-all ${active ? 'bg-[#FF4B6B] shadow-[0_4px_10px_rgba(255,75,107,0.3)]' : 'bg-[#FFEBEE]'}`}>
    <svg width="12" height="12" viewBox="0 0 24 24" fill={active ? "white" : "#FF4B6B"} stroke="none">
       <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
    </svg>
    <span className={`${active ? 'text-white' : 'text-[#FF4B6B]'} text-[11px] font-bold tracking-tighter`}>{percent}</span>
  </div>
);

// 新版首页需要的图标
const IconStoreBox = () => (
  <div className="w-[46px] h-[46px] relative">
    <div className="absolute inset-0 bg-gradient-to-b from-[#A5C0FF] to-[#7196FF] rounded-2xl opacity-30 blur-[6px] transform translate-y-1"></div>
    <div className="absolute inset-0 bg-gradient-to-b from-[#B8CDFF] to-[#6A8EFF] rounded-2xl border border-white shadow-sm flex items-center justify-center overflow-hidden">
      <div className="absolute top-0 w-full h-[18px] bg-[#E0E9FF] opacity-50 rounded-b-[10px]"></div>
      <div className="w-6 h-[5px] bg-white rounded-full mt-3 opacity-90"></div>
    </div>
  </div>
);

const IconBookmark = () => (
  <div className="w-[46px] h-[46px] relative">
    <div className="absolute inset-0 bg-gradient-to-b from-[#FFB8A5] to-[#FF8171] rounded-2xl opacity-30 blur-[6px] transform translate-y-1"></div>
    <div className="absolute inset-0 bg-gradient-to-b from-[#FFD2C2] to-[#FF8C7A] rounded-2xl border border-white shadow-sm flex flex-col items-center pt-[6px]">
      <svg width="22" height="28" viewBox="0 0 24 32" fill="none">
        <path d="M4 2C4 0.89543 4.89543 0 6 0H18C19.1046 0 20 0.89543 20 2V30.5528C20 31.4477 18.918 31.8924 18.2891 31.2599L12 24.9348L5.7109 31.2599C5.08203 31.8924 4 31.4477 4 30.5528V2Z" fill="#FFF1EB"/>
        <rect x="8" y="6" width="8" height="4" rx="2" fill="#FFB8A5"/>
      </svg>
    </div>
  </div>
);

const IconTShirt = () => (
  <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <path d="M20.38 3.46L16 2a4 4 0 0 1-8 0L3.62 3.46a2 2 0 0 0-1.34 2.23l.58 3.47a1 1 0 0 0 .99.84H6v10c0 1.1.9 2 2 2h8a2 2 0 0 0 2-2V10h2.15a1 1 0 0 0 .99-.84l.58-3.47a2 2 0 0 0-1.34-2.23z" />
    <path d="M11 15l2-4-3-1 2-4" strokeWidth="1.5" />
  </svg>
);

const IconKeyboardGrid = () => (
  <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="4" width="20" height="16" rx="4" />
    <circle cx="8" cy="10" r="1" fill="#333" stroke="none" />
    <circle cx="12" cy="10" r="1" fill="#333" stroke="none" />
    <circle cx="16" cy="10" r="1" fill="#333" stroke="none" />
    <circle cx="8" cy="14" r="1" fill="#333" stroke="none" />
    <circle cx="12" cy="14" r="1" fill="#333" stroke="none" />
    <circle cx="16" cy="14" r="1" fill="#333" stroke="none" />
  </svg>
);

const IconFAQ = () => (
  <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="#333" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="12" cy="12" r="10" />
    <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" />
    <circle cx="12" cy="17" r="1" fill="#333" stroke="none" />
  </svg>
);

const IconTicket = () => (
  <svg width="48" height="42" viewBox="0 0 46 40" fill="none">
    <path d="M6 10C6 7.79086 7.79086 6 10 6H36C38.2091 6 40 7.79086 40 10V14C37.7909 14 36 15.7909 36 18C36 20.2091 37.7909 22 40 22V28C40 30.2091 38.2091 32 36 32H10C7.79086 32 6 30.2091 6 28V22C8.20914 22 10 20.2091 10 18C10 15.7909 8.20914 14 6 14V10Z" fill="url(#ticket-gradient)" transform="rotate(-15 23 19)" />
    <text x="14" y="27" fill="white" fontSize="20" fontWeight="900" transform="rotate(-15 23 19)" style={{fontStyle: 'italic'}}>%</text>
    <defs>
      <linearGradient id="ticket-gradient" x1="6" y1="6" x2="40" y2="32" gradientUnits="userSpaceOnUse">
        <stop stopColor="#F871FF" />
        <stop offset="1" stopColor="#C955F7" />
      </linearGradient>
    </defs>
  </svg>
);

const AppNavIconChat = ({ active }) => (
  <svg width={active ? "30" : "24"} height={active ? "30" : "24"} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={active ? "2.5" : "2"} strokeLinecap="round" strokeLinejoin="round">
    <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" fill={active ? "#E8EDFF" : "none"}></path>
    <circle cx="9" cy="11" r="1.5" fill="currentColor" stroke="none" />
    <circle cx="15" cy="11" r="1.5" fill="currentColor" stroke="none" />
  </svg>
);

const AppNavIconMarket = ({ active }) => (
  <svg width={active ? "30" : "24"} height={active ? "30" : "24"} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={active ? "2.5" : "2"} strokeLinecap="round" strokeLinejoin="round">
    <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
    <polyline points="9 22 9 12 15 12 15 22"></polyline>
  </svg>
);

const AppNavIconKeyboard = ({ active }) => (
  <svg width={active ? "30" : "24"} height={active ? "30" : "24"} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth={active ? "2.5" : "2"} strokeLinecap="round" strokeLinejoin="round">
    <rect x="2" y="6" width="20" height="12" rx="3" />
    <circle cx="7" cy="10" r="1" fill="currentColor" stroke="none" />
    <circle cx="12" cy="10" r="1" fill="currentColor" stroke="none" />
    <circle cx="17" cy="10" r="1" fill="currentColor" stroke="none" />
    <rect x="7" y="13" width="10" height="2" rx="1" fill="currentColor" stroke="none" />
  </svg>
);

const NavTabButton = ({ active, onClick, icon: Icon, label }) => (
  <div onClick={onClick} className={`flex-1 flex flex-col items-center justify-start cursor-pointer transition-colors relative ${active ? 'text-[#5C73FF]' : 'text-[#999]'}`}>
    <div className={`relative flex items-center justify-center transition-all duration-300 z-10 ${
      active 
        ? 'w-[64px] h-[64px] -mt-[24px] mb-[4px] rounded-[22px] bg-gradient-to-br from-[#FFFFFF] to-[#E2E6F2] shadow-[8px_8px_16px_rgba(160,175,210,0.6),-8px_-8px_16px_rgba(255,255,255,1),inset_2px_2px_4px_rgba(255,255,255,0.9)] border border-white/80' 
        : 'w-[42px] h-[42px] mt-0 mb-1 rounded-[16px] bg-transparent shadow-none border border-transparent'
    }`}>
      <Icon active={active} />
    </div>
    <span className="text-[11px] font-bold">{label}</span>
  </div>
);

const WeChatIcon = () => (
  <div className="w-10 h-10 rounded-full bg-[#E5E5E5] flex items-center justify-center cursor-pointer hover:opacity-80 transition-opacity">
    <svg width="22" height="22" viewBox="0 0 24 24" fill="#666">
      <path d="M8.5 15.5C5.46 15.5 3 13.37 3 10.75C3 8.13 5.46 6 8.5 6C11.54 6 14 8.13 14 10.75C14 13.37 11.54 15.5 8.5 15.5ZM17 17.5C14.79 17.5 13 16.04 13 14.25C13 12.46 14.79 11 17 11C19.21 11 21 12.46 21 14.25C21 16.04 19.21 17.5 17 17.5Z"/>
      <circle cx="6.5" cy="9.5" r="0.8" fill="white"/><circle cx="10.5" cy="9.5" r="0.8" fill="white"/>
      <circle cx="15.5" cy="13.5" r="0.6" fill="white"/><circle cx="18.5" cy="13.5" r="0.6" fill="white"/>
    </svg>
  </div>
);

const Heart3D = () => (
  <div className="relative w-[280px] h-[280px] flex justify-center items-center">
    {/* 背部环境光晕 */}
    <div className="absolute w-[160px] h-[160px] bg-[#FF4B6B] rounded-full blur-[50px] opacity-30 animate-pulse-soft"></div>
    
    <svg width="100%" height="100%" viewBox="0 0 200 200" fill="none" className="relative z-10 drop-shadow-[0_20px_35px_rgba(255,75,107,0.25)] animate-float-y">
      <defs>
        <linearGradient id="heartGlass" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stopColor="#FFFFFF" stopOpacity="0.95" />
          <stop offset="30%" stopColor="#FFFFFF" stopOpacity="0.4" />
          <stop offset="70%" stopColor="#F5DFFF" stopOpacity="0.2" />
          <stop offset="100%" stopColor="#FFF0F5" stopOpacity="0.8" />
        </linearGradient>
        <linearGradient id="heartBorder" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stopColor="#FFFFFF" stopOpacity="1" />
          <stop offset="50%" stopColor="#FFFFFF" stopOpacity="0.2" />
          <stop offset="100%" stopColor="#FFFFFF" stopOpacity="0.9" />
        </linearGradient>
        <linearGradient id="liquidGrad1" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#FF7CA1" stopOpacity="0.9" />
          <stop offset="100%" stopColor="#FF4B6B" stopOpacity="1" />
        </linearGradient>
        <linearGradient id="liquidGrad2" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stopColor="#FF4B6B" />
          <stop offset="100%" stopColor="#D6183D" />
        </linearGradient>
        <filter id="blurLight">
          <feGaussianBlur stdDeviation="3" />
        </filter>
        <clipPath id="heartMask">
          <path d="M100 170 C 100 170, 20 110, 20 55 C 20 25, 50 10, 75 10 C 90 10, 100 20, 100 20 C 100 20, 110 10, 125 10 C 150 10, 180 25, 180 55 C 180 110, 100 170, 100 170 Z" />
        </clipPath>
      </defs>
      
      {/* 玻璃外壳底层 */}
      <path d="M100 170 C 100 170, 20 110, 20 55 C 20 25, 50 10, 75 10 C 90 10, 100 20, 100 20 C 100 20, 110 10, 125 10 C 150 10, 180 25, 180 55 C 180 110, 100 170, 100 170 Z" fill="url(#heartGlass)" />
      
      {/* 内部流体及动画波浪 */}
      <g clipPath="url(#heartMask)">
        <g style={{ animation: 'heart-liquid-pulse 3s ease-in-out infinite', transformOrigin: '50% 170px' }}>
           {/* 底部基色 */}
           <rect x="0" y="130" width="200" height="70" fill="url(#liquidGrad2)" />
           
           {/* 后层波浪 */}
           <g style={{ animation: 'heart-wave-reverse 4s linear infinite' }}>
             <path d="M -100 126 Q -75 134, -50 126 T 0 126 T 50 126 T 100 126 T 150 126 T 200 126 L 200 200 L -100 200 Z" fill="url(#liquidGrad1)" opacity="0.7" />
           </g>

           {/* 前层波浪 */}
           <g style={{ animation: 'heart-wave 3s linear infinite' }}>
             <path d="M 0 130 Q 25 120, 50 130 T 100 130 T 150 130 T 200 130 T 250 130 T 300 130 L 300 200 L 0 200 Z" fill="url(#liquidGrad2)" />
           </g>

           {/* 上浮的气泡 */}
           <circle cx="85" cy="145" r="3" fill="white" opacity="0" style={{ animation: 'float-bubble 2.5s ease-in infinite' }} />
           <circle cx="115" cy="155" r="4.5" fill="white" opacity="0" style={{ animation: 'float-bubble 3.2s ease-in infinite 1s' }} />
           <circle cx="95" cy="140" r="2" fill="white" opacity="0" style={{ animation: 'float-bubble 2s ease-in infinite 0.7s' }} />
        </g>
      </g>

      {/* 3D加厚内发光描边 */}
      <path d="M100 170 C 100 170, 20 110, 20 55 C 20 25, 50 10, 75 10 C 90 10, 100 20, 100 20 C 100 20, 110 10, 125 10 C 150 10, 180 25, 180 55 C 180 110, 100 170, 100 170 Z" fill="none" stroke="url(#heartBorder)" strokeWidth="3" />
      
      {/* 左上角柔和/清脆高光 (模拟玻璃反射) */}
      <path d="M70 14 C 45 14, 25 35, 23 60 C 22 75, 28 88, 35 100" fill="none" stroke="white" strokeWidth="8" strokeLinecap="round" filter="url(#blurLight)" opacity="0.9" />
      <path d="M70 14 C 45 14, 25 35, 23 60 C 22 75, 28 88, 35 100" fill="none" stroke="white" strokeWidth="2.5" strokeLinecap="round" opacity="0.95" />

      {/* 右上角副高光 */}
      <path d="M145 16 C 160 21, 175 35, 175 55" fill="none" stroke="white" strokeWidth="4" strokeLinecap="round" filter="url(#blurLight)" opacity="0.7" />
      <path d="M145 16 C 160 21, 175 35, 175 55" fill="none" stroke="white" strokeWidth="1.5" strokeLinecap="round" opacity="0.8" />
    </svg>
  </div>
);


// ==========================================
// 共用组件 (Tag, WheelPicker, 预览键盘等)
// ==========================================
const WheelPicker = ({ items, value, onChange, unit }) => {
  const containerRef = useRef(null);
  const itemHeight = 56; 
  const isAutoScrolling = useRef(false);

  useEffect(() => {
    const index = items.indexOf(value);
    if (index !== -1 && containerRef.current) {
      const targetTop = index * itemHeight;
      if (Math.abs(containerRef.current.scrollTop - targetTop) > 2) {
        isAutoScrolling.current = true;
        const timer = setTimeout(() => {
          if (containerRef.current) {
            containerRef.current.scrollTo({ top: targetTop, behavior: 'instant' });
            setTimeout(() => { isAutoScrolling.current = false; }, 50);
          }
        }, 50);
        return () => clearTimeout(timer);
      }
    }
  }, [items, value]);

  const handleScroll = (e) => {
    if (isAutoScrolling.current) return;
    const index = Math.round(e.target.scrollTop / itemHeight);
    if (items[index] !== undefined && items[index] !== value) {
      onChange(items[index]);
    }
  };

  return (
    <div className="relative h-[168px] w-full flex-1 overflow-hidden z-10">
      <div ref={containerRef} onScroll={handleScroll} className="h-full overflow-y-scroll snap-y snap-mandatory no-scrollbar" style={{ scrollBehavior: 'auto' }}>
        <div style={{ height: `${itemHeight}px` }}></div>
        {items.map((item, i) => {
          const displayValue = unit !== '年' ? String(item).padStart(2, '0') : item;
          const isSelected = item === value;
          return (
            <div key={i} style={{ height: `${itemHeight}px` }} className={`flex items-center justify-center snap-center transition-all duration-300 ${isSelected ? 'text-[#1A1A1A] font-bold text-[20px]' : 'text-[#C0C4D0] font-bold text-[16px]'}`}>
              {displayValue}{unit}
            </div>
          );
        })}
        <div style={{ height: `${itemHeight}px` }}></div>
      </div>
    </div>
  );
};

const Tag = memo(({ icon, text, hot, id, isSelected, onToggle }) => (
  <div onClick={() => onToggle?.(id)} className="relative flex-shrink-0 cursor-pointer active:scale-95 px-2.5 py-1">
    <div className={`h-[54px] px-6 rounded-full flex items-center space-x-2.5 transition-all duration-300 ${
      isSelected ? 'bg-[#667EFE] text-white shadow-[0_6px_16px_rgba(102,126,254,0.4)]' : 'bg-white text-[#1A1A1A] shadow-[0_4px_12px_rgba(0,0,0,0.03)]'
    }`}>
       {icon && <span className="text-[20px] leading-none">{icon}</span>}
       <span className={`font-medium text-[16px] whitespace-nowrap ${isSelected ? 'text-white' : 'text-[#333]'}`}>{text}</span>
    </div>
    {hot && (
      <div className={`absolute top-0 right-1 bg-gradient-to-r from-[#FF923D] to-[#FF452F] text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full border-2 ${isSelected ? 'border-[#667EFE]' : 'border-white'} transition-colors duration-300 z-10`}>
        HOT
      </div>
    )}
  </div>
));

const KeyboardPreviewWrapper = ({ children }) => (
  <div className="w-full h-[180px] rounded-[16px] bg-[#F2F4F9] relative overflow-hidden flex flex-col p-2 select-none">
    <div className="absolute -top-10 -left-10 w-40 h-40 bg-[#E1C8FF] rounded-full mix-blend-multiply filter blur-2xl opacity-60"></div>
    <div className="absolute top-10 right-0 w-48 h-48 bg-[#C8DBFF] rounded-full mix-blend-multiply filter blur-2xl opacity-60"></div>
    <div className="absolute -bottom-10 left-10 w-32 h-32 bg-[#FFE1E1] rounded-full mix-blend-multiply filter blur-2xl opacity-50"></div>
    <div className="relative z-10 w-full h-full">{children}</div>
  </div>
);

const NineGridPreview = () => (
  <KeyboardPreviewWrapper>
    <div className="grid grid-cols-[1.3fr_3fr_1.5fr] gap-1.5 h-full">
      <div className="flex flex-col gap-1.5">{['，', '。', '?', '!', '符号'].map((key, i) => (<div key={i} className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[12px] font-medium shadow-sm">{key}</div>))}</div>
      <div className="grid grid-cols-3 gap-1.5">
        {['@#', 'ABC', 'DEF', 'GHI', 'JKL', 'MNO', 'PQRS', 'TUV', 'WXYZ'].map((key, i) => (<div key={i} className="bg-white text-[#1A1A1A] rounded-[8px] flex flex-col items-center justify-center text-[12px] font-semibold shadow-sm">{key}</div>))}
        <div className="bg-white text-[#1A1A1A] rounded-[8px] flex items-center justify-center text-[12px] font-semibold shadow-sm">123</div>
        <div className="bg-white text-[#A0A5B5] rounded-[8px] flex items-center justify-center shadow-sm"><KeyIconMic /></div>
        <div className="bg-white text-[#1A1A1A] rounded-[8px] flex items-center justify-center text-[11px] font-semibold shadow-sm leading-tight text-center">中<br/>英</div>
      </div>
      <div className="flex flex-col gap-1.5">
        <div className="h-[22%] bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center shadow-sm"><KeyIconBackspace /></div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[12px] font-medium shadow-sm">换行</div>
        <div className="flex-1 bg-[#AEC0FF] text-white/90 rounded-[6px] flex items-center justify-center text-[12px] font-semibold shadow-sm">发送</div>
      </div>
    </div>
  </KeyboardPreviewWrapper>
);

const FullKeyboardPreview = () => (
  <KeyboardPreviewWrapper>
    <div className="flex flex-col gap-1.5 h-full pt-1">
      <div className="flex justify-center gap-1">{['Q','W','E','R','T','Y','U','I','O','P'].map(k => (<div key={k} className="flex-1 bg-white text-[#1A1A1A] h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-bold shadow-sm">{k}</div>))}</div>
      <div className="flex justify-center gap-1 px-[4%]">{['A','S','D','F','G','H','J','K','L'].map(k => (<div key={k} className="flex-1 bg-white text-[#1A1A1A] h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-bold shadow-sm">{k}</div>))}</div>
      <div className="flex justify-center gap-1">
        <div className="flex-[1.5] bg-[#D3D8E6] text-[#333] h-[34px] rounded-[6px] flex items-center justify-center shadow-sm"><KeyIconShift /></div>
        {['Z','X','C','V','B','N','M'].map(k => (<div key={k} className="flex-1 bg-white text-[#1A1A1A] h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-bold shadow-sm">{k}</div>))}
        <div className="flex-[1.5] bg-[#D3D8E6] text-[#333] h-[34px] rounded-[6px] flex items-center justify-center shadow-sm"><KeyIconBackspace /></div>
      </div>
      <div className="flex justify-center gap-1 mt-auto">
        <div className="flex-[2] bg-[#D3D8E6] text-[#333] h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-medium shadow-sm">123</div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-medium shadow-sm">,</div>
        <div className="flex-[5] bg-white text-[#A0A5B5] h-[34px] rounded-[6px] flex items-center justify-center shadow-sm"><KeyIconMic /></div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] h-[34px] rounded-[6px] flex items-center justify-center text-[10px] font-medium shadow-sm leading-none text-center">中<br/>英</div>
        <div className="flex-[2] bg-[#AEC0FF] text-white/90 h-[34px] rounded-[6px] flex items-center justify-center text-[12px] font-semibold shadow-sm">发送</div>
      </div>
    </div>
  </KeyboardPreviewWrapper>
);

const HandwritingPreview = () => (
  <KeyboardPreviewWrapper>
    <div className="flex flex-col h-full">
      <div className="flex-1 relative flex">
        <div className="flex-1 flex items-center justify-center"><span className="text-6xl font-light text-[#2C334A] tracking-[10px] transform -rotate-[8deg] drop-shadow-sm font-serif" style={{ fontFamily: '"Brush Script MT", "STKaiti", cursive' }}>你好</span></div>
        <div className="w-[30px] flex flex-col gap-1 pr-1">
          <div className="h-[24px] bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center shadow-sm mt-1"><KeyIconBackspace /></div>
          <div className="flex-1 flex flex-col justify-around text-[#555] font-bold text-[12px] items-center pb-2"><span>，</span><span>。</span><span>?</span><span>!</span></div>
        </div>
      </div>
      <div className="h-[34px] flex gap-1 mt-1">
        <div className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[11px] font-medium shadow-sm">符号</div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[11px] font-medium shadow-sm">123</div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[11px] font-medium shadow-sm">ABC</div>
        <div className="flex-[2] bg-white rounded-[6px] shadow-sm"></div>
        <div className="flex-1 bg-[#D3D8E6] text-[#333] rounded-[6px] flex items-center justify-center text-[11px] font-medium shadow-sm text-center">写</div>
        <div className="flex-[1.5] bg-[#5C73FF] text-white rounded-[6px] flex items-center justify-center text-[12px] font-semibold shadow-sm">发送</div>
      </div>
    </div>
  </KeyboardPreviewWrapper>
);

const BrushStrokeBg = ({ children, className }) => (
  <div className={`relative inline-flex items-center justify-center ${className}`}>
    <svg className="absolute w-full h-full min-w-[120%]" viewBox="0 0 200 60" preserveAspectRatio="none" fill="none">
      <path d="M10,30 Q40,15 100,20 T190,25 Q185,45 120,40 T15,45 Z" fill="#3B5BFF" opacity="0.95" />
      <path d="M5,25 Q50,5 150,15 T195,35 Q170,55 80,50 T5,35 Z" fill="#4B6BFF" opacity="0.8" />
    </svg>
    <span className="relative z-10 brush-text">{children}</span>
  </div>
);

const CurvedArrow = ({ color = "#FFD233", type = "down" }) => {
  if (type === "down") {
    return (
      <svg width="40" height="60" viewBox="0 0 40 60" fill="none" className="drop-shadow-md">
        <path d="M 30 10 Q 35 30 10 50" stroke={color} strokeWidth="4" strokeLinecap="round" fill="none" style={{ strokeDasharray: 100, animation: 'draw-arrow 0.6s ease-out forwards' }} />
        <path d="M 10 50 L 18 42 M 10 50 L 20 54" stroke={color} strokeWidth="4" strokeLinecap="round" />
      </svg>
    );
  }
  if (type === "up-left") {
    return (
      <svg width="60" height="60" viewBox="0 0 60 60" fill="none" className="drop-shadow-md">
        <path d="M 50 50 Q 20 40 10 10" stroke={color} strokeWidth="4" strokeLinecap="round" fill="none" style={{ strokeDasharray: 100, animation: 'draw-arrow 0.6s ease-out forwards' }} />
        <path d="M 10 10 L 20 12 M 10 10 L 12 20" stroke={color} strokeWidth="4" strokeLinecap="round" />
      </svg>
    );
  }
  if (type === "left-up") {
    return (
      <svg width="60" height="60" viewBox="0 0 60 60" fill="none" className="drop-shadow-md">
        <path d="M 50 50 Q 20 45 15 15" stroke={color} strokeWidth="4" strokeLinecap="round" fill="none" style={{ strokeDasharray: 100, animation: 'draw-arrow 0.6s ease-out forwards' }} />
        <path d="M 15 15 L 25 18 M 15 15 L 12 25" stroke={color} strokeWidth="4" strokeLinecap="round" />
      </svg>
    );
  }
  return (
    <svg width="60" height="60" viewBox="0 0 60 60" fill="none" className="drop-shadow-md">
      <path d="M 10 10 Q 40 15 50 50" stroke={color} strokeWidth="4" strokeLinecap="round" fill="none" style={{ strokeDasharray: 100, animation: 'draw-arrow 0.6s ease-out forwards' }} />
      <path d="M 50 50 L 42 42 M 50 50 L 54 40" stroke={color} strokeWidth="4" strokeLinecap="round" />
    </svg>
  );
};


// ==========================================
// 引导页流程组件 (Step 0 ~ 9)
// ==========================================
const WelcomeScreen = ({ onNext }) => (
  <div className="relative w-full h-full bg-gradient-to-b from-[#4A64FF] to-[#6E82FF] flex flex-col justify-between items-center px-9 py-16 text-white overflow-hidden">
    <div className="absolute top-0 left-0 w-full h-full pointer-events-none opacity-[0.08] mix-blend-overlay"><div className="absolute top-[-5%] left-[-15%] text-[180px] font-black italic transform rotate-[-12deg]">Love</div><div className="absolute top-[18%] left-[20%] text-[180px] font-black italic transform rotate-[-12deg]">Key</div></div>
    <div className="relative z-10 w-full mt-8 text-left">
      <div className="relative w-36 h-36 mb-10">
        <div className="absolute bottom-0 left-0 w-[130px] h-[86px] bg-white/40 backdrop-blur-xl rounded-[28px] shadow-lg flex items-center justify-center border border-white/30"><div className="grid grid-cols-4 gap-2 opacity-30">{[...Array(12)].map((_,i) => <div key={i} className="w-4 h-4 rounded-[4px] bg-white"></div>)}</div></div>
        <div className="absolute top-0 left-6 w-[88px] h-[88px] bg-[#FFE03D] rounded-full shadow-md flex items-center justify-center"><div className="flex flex-col items-center pt-2"><div className="flex space-x-5 mb-1"><div className="w-2.5 h-2.5 bg-gray-800 rounded-full"></div><div className="w-2.5 h-1.5 bg-gray-800 rounded-b-full"></div></div><div className="w-8 h-4 border-b-4 border-gray-800 rounded-full"></div></div></div>
      </div>
      <h1 className="text-[34px] font-bold mb-5 tracking-tight">欢迎来到Lovekey</h1>
      <p className="text-[14px] leading-relaxed text-white/80">为了给您提供更全面的服务。我们将通过<span className="font-bold text-white">《用户协议》</span>和<span className="font-bold text-white">《隐私协议》</span>了解您的个人信息情况。</p>
    </div>
    <button onClick={onNext} className="w-full bg-black text-white rounded-full py-4.5 text-[18px] font-bold active:scale-[0.98] transition-all cursor-pointer" style={{ padding: '1.25rem' }}>同意并进入</button>
  </div>
);

const GenderScreen = ({ onNext }) => {
  const [selected, setSelected] = useState('male');
  return (
    <div className="w-full h-full bg-[#F6F8FD] flex flex-col items-center px-6 py-12 relative overflow-hidden">
      <div className="flex space-x-2.5 mt-2 mb-14"><div className="w-1.5 h-1.5 bg-[#1A1A1A]"></div><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div></div>
      <h2 className="text-[28px] font-bold text-[#1A1A1A] mb-12 tracking-wide">选择性别</h2>
      <div className="w-full flex justify-center space-x-5 px-2">
        <div className="flex flex-col items-center cursor-pointer group" onClick={() => setSelected('male')}>
          <div className={`w-[145px] h-[145px] bg-white rounded-[26px] flex items-center justify-center transition-all duration-300 ${selected === 'male' ? 'border-[2.5px] border-[#7F95FF]' : 'border-[2.5px] border-transparent shadow-[0_4px_16px_rgba(0,0,0,0.04)]'}`}><div className={`w-full h-full flex items-center justify-center transition-transform duration-300 ${selected === 'male' ? 'scale-105' : 'scale-100'}`}><MaleAvatar /></div></div>
          <span className={`mt-6 font-medium text-[17px] transition-colors ${selected === 'male' ? 'text-[#1A1A1A]' : 'text-[#666]'}`}>男</span>
        </div>
        <div className="flex flex-col items-center cursor-pointer group" onClick={() => setSelected('female')}>
          <div className={`w-[145px] h-[145px] bg-white rounded-[26px] flex items-center justify-center transition-all duration-300 ${selected === 'female' ? 'border-[2.5px] border-[#7F95FF]' : 'border-[2.5px] border-transparent shadow-[0_4px_16px_rgba(0,0,0,0.04)]'}`}><div className={`w-full h-full flex items-center justify-center transition-transform duration-300 ${selected === 'female' ? 'scale-105' : 'scale-100'}`}><FemaleAvatar /></div></div>
          <span className={`mt-6 font-medium text-[17px] transition-colors ${selected === 'female' ? 'text-[#1A1A1A]' : 'text-[#666]'}`}>女</span>
        </div>
      </div>
      <div className="mt-auto mb-10"><button onClick={onNext} className="w-[110px] h-[54px] bg-[#667EFE] rounded-full flex items-center justify-center shadow-[0_8px_20px_rgba(102,126,254,0.3)] text-white active:scale-95 transition-transform cursor-pointer"><ArrowRight size={24} strokeWidth={2.5} /></button></div>
    </div>
  );
};

const BirthdayScreen = ({ onPrev, onNext }) => {
  const [year, setYear] = useState(2006);
  const [month, setMonth] = useState(3);
  const [day, setDay] = useState(16);
  const years = useMemo(() => Array.from({ length: 60 }, (_, i) => 1970 + i), []);
  const months = useMemo(() => Array.from({ length: 12 }, (_, i) => i + 1), []);
  const days = useMemo(() => Array.from({ length: new Date(year, month, 0).getDate() }, (_, i) => i + 1), [year, month]);
  
  return (
    <div className="w-full h-full bg-[#F6F8FD] flex flex-col items-center px-6 py-12 relative overflow-hidden">
      <div className="absolute top-12 w-full px-6 flex items-center justify-between"><button onClick={onPrev} className="w-[42px] h-[42px] bg-white rounded-[14px] flex items-center justify-center shadow-[0_2px_8px_rgba(0,0,0,0.04)] text-gray-800 cursor-pointer"><ChevronLeft size={22} strokeWidth={2.5}/></button><div className="flex space-x-2 absolute left-1/2 transform -translate-x-1/2"><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div><div className="w-1.5 h-1.5 bg-[#1A1A1A]"></div><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div></div></div>
      <div className="mt-14 text-center flex flex-col items-center"><div className="mb-4"><CakeIcon /></div><h2 className="text-[28px] font-bold text-[#1A1A1A] tracking-wide">你的生日在哪一天呢</h2></div>
      <div className="w-full mt-10 relative px-4"><div className="absolute top-1/2 left-3 right-3 h-[58px] bg-white rounded-[14px] shadow-[0_4px_16px_rgba(0,0,0,0.02)] -translate-y-1/2 z-0 pointer-events-none"></div><div className="flex justify-between items-center relative h-[168px]"><WheelPicker items={years} value={year} onChange={setYear} unit="年" /><WheelPicker items={months} value={month} onChange={setMonth} unit="月" /><WheelPicker items={days} value={day} onChange={setDay} unit="日" /></div></div>
      <div className="mt-auto flex flex-col items-center w-full mb-10"><div className="mb-14 text-[20px] font-bold text-[#1A1A1A] tracking-wider flex space-x-4"><span>{getAge(year)}岁</span><span>{getZodiac(month, day)}</span></div><button onClick={onNext} className="w-[110px] h-[54px] bg-[#667EFE] rounded-full flex items-center justify-center text-white shadow-[0_8px_20px_rgba(102,126,254,0.3)] active:scale-95 transition-transform cursor-pointer"><ArrowRight size={24} strokeWidth={2.5}/></button></div>
    </div>
  );
};

const PersonaScreen = ({ onPrev, onNext, selectedIds, onToggle }) => {
  const isValidSelection = selectedIds.length >= 1;
  return (
    <div className="w-full h-full bg-[#F6F8FD] flex flex-col relative overflow-hidden">
      <div className="pt-12 px-6 flex items-center justify-between">
        <button onClick={onPrev} className="w-[42px] h-[42px] bg-white rounded-[14px] flex items-center justify-center shadow-[0_2px_8px_rgba(0,0,0,0.04)] text-gray-800 cursor-pointer"><ChevronLeft size={22} strokeWidth={2.5}/></button>
        <div className="flex space-x-2 absolute left-1/2 -translate-x-1/2"><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div><div className="w-1.5 h-1.5 bg-[#DCDFE6]"></div><div className="w-1.5 h-1.5 bg-[#1A1A1A]"></div></div>
      </div>
      <div className="mt-12 px-10 text-center mb-8"><h2 className="text-[28px] font-bold text-[#1A1A1A] tracking-wide">添加喜欢的「人设」到键盘</h2></div>
      <div className="flex flex-col gap-y-0 w-full py-1 overflow-visible">
        {GROUPS.map((group, idx) => {
          const duration = `${30 + idx * 8}s`; 
          return (
            <div key={idx} className="w-full overflow-hidden flex relative">
              <div className="animate-marquee-left" style={{ animationDuration: duration }}>
                <div className="flex px-2">
                  {[...group, ...group, ...group, ...group].map((item, i) => (
                    <Tag key={`${item.id}-${i}`} {...item} isSelected={selectedIds.includes(item.id)} onToggle={onToggle} />
                  ))}
                </div>
              </div>
            </div>
          );
        })}
      </div>
      <div className="mt-auto px-6 mb-10 w-full flex justify-center">
        <button 
          onClick={onNext} disabled={!isValidSelection} 
          className={`w-[90%] h-[56px] rounded-full text-[17px] font-bold tracking-wide transition-all duration-300 cursor-pointer ${
            isValidSelection ? 'bg-[#667EFE] text-white shadow-[0_8px_20px_rgba(102,126,254,0.3)] active:scale-95' : 'bg-[#A8ADB8] text-white opacity-90'
          }`}
        >
          {isValidSelection ? '生成专属聊天键盘' : '请至少选择 1 个人设'}
        </button>
      </div>
    </div>
  );
};

const LoadingScreen = ({ onNext }) => {
  const [phase, setPhase] = useState('loading');
  const [showCheck, setShowCheck] = useState(false);
  
  const flyTags = useMemo(() => [
    { id: 't1', icon: "😊", text: "温柔体贴", delay: 0.2, startX: -60, rot: -8 },
    { id: 't2', icon: "😘", text: "暧昧拉扯", delay: 0.6, startX: 50,  rot: 5 },
    { id: 't3', icon: "👍", text: "幽默",     delay: 1.0, startX: -40, rot: -4 },
    { id: 't4', icon: "✨", text: "高情商",   delay: 1.4, startX: 60,  rot: 6 },
    { id: 't5', icon: "🙄", text: "怼一下",   delay: 1.8, startX: -50, rot: -5 },
    { id: 't6', icon: "💎", text: "双商在线", delay: 2.2, startX: 30,  rot: 3 }
  ], []);

  useEffect(() => {
    const successTimer = setTimeout(() => setPhase('success'), 3500);
    const popTimer = setTimeout(() => setShowCheck(true), 3500);
    const nextTimer = setTimeout(() => onNext(), 6500); // 延长了进入下一步的时间，留给烟花表演

    return () => { clearTimeout(successTimer); clearTimeout(popTimer); clearTimeout(nextTimer); };
  }, [onNext]);

  return (
    <div className="w-full h-full bg-gradient-to-b from-[#EAEFFF] to-[#E2E8FF] relative overflow-hidden flex flex-col items-center justify-center">
      <SparkleIcon className="absolute w-5 h-5 top-[35%] right-[20%] animate-star z-10 text-[#FFF280]" style={{ animationDelay: '0s' }} />
      <SparkleIcon className="absolute w-3 h-3 top-[50%] left-[28%] animate-star z-10 text-[#FFF280]" style={{ animationDelay: '0.8s' }} />
      <SparkleIcon className="absolute w-4 h-4 bottom-[38%] right-[30%] animate-star z-10 text-[#FFF280]" style={{ animationDelay: '1.5s' }} />

      <div className="w-full relative flex flex-col items-center z-20 -mt-16">
        <div className="relative w-full h-[320px] flex justify-center items-end pb-6">
          {flyTags.map((tag) => (
            <div key={tag.id} className="absolute z-40 pointer-events-none flex justify-center" style={{ '--startX': `${tag.startX}px`, '--startRot': `${tag.rot * 2}deg`, animation: `bubble-absorb 1.2s cubic-bezier(0.34, 1.56, 0.64, 1) forwards`, animationDelay: `${tag.delay}s`, opacity: 0, bottom: '40px' }}>
              <div className="bg-white/60 backdrop-blur-md px-4 py-2 rounded-full shadow-[0_6px_20px_rgba(110,130,240,0.12)] border border-white/70 flex items-center space-x-1.5">
                <span className="text-[16px] leading-none">{tag.icon}</span>
                <span className="font-bold text-[#555] text-[14px]">{tag.text}</span>
              </div>
            </div>
          ))}
          <div className="absolute bottom-10 z-30" style={{ animation: 'keyboard-breathe 3s ease-in-out infinite' }}>
            <Keyboard3DGraphic />
          </div>
          <div className="absolute bottom-[110px] z-[100] flex items-center justify-center" style={{ opacity: 0, transform: 'scale(0)', animation: showCheck ? 'check-pop 0.5s cubic-bezier(0.34, 1.56, 0.64, 1) forwards' : 'none' }}>
             <CheckCircle3DGraphic />
             {showCheck && <FireworkBurst />}
          </div>
        </div>

        <div className="w-full h-[80px] relative flex justify-center z-50 mt-10">
          <div className="absolute flex flex-col items-center" style={{ animation: phase === 'success' ? 'text-fade-out 0.3s ease-in forwards' : 'none' }}>
             <h2 className="text-[#3A4B86] text-[18px] font-bold mb-1.5 tracking-wide">正在为您组建键盘...</h2>
             <p className="text-[#8C9AD6] text-[13px] font-medium tracking-wide">正在根据您的选择组建专属键盘</p>
          </div>
          <div className="absolute flex flex-col items-center" style={{ opacity: 0, transform: 'translateY(10px)', animation: phase === 'success' ? 'text-fade-in 0.4s ease-out forwards 0.2s' : 'none' }}>
             <h2 className="text-[#3A4B86] text-[18px] font-bold tracking-wide mt-1">组建完成</h2>
          </div>
        </div>
      </div>
    </div>
  );
};

const SetupKeyboardScreen = ({ onNext }) => {
  const [setupStep, setSetupStep] = useState(1);
  const [showBottomSheet, setShowBottomSheet] = useState(false);

  const handleStep1Click = () => {
    if (setupStep === 1) {
      setTimeout(() => setSetupStep(2), 600);
    }
  };

  return (
    <div className="w-full h-full bg-gradient-to-b from-[#5C73FF] to-[#455DF8] relative flex flex-col overflow-hidden">
      <div className="absolute inset-0 pointer-events-none flex flex-col justify-between -z-0 opacity-[0.06]">
        <div className="text-[140px] font-black italic -rotate-12 mt-2 -ml-6 text-white">KNOW</div>
        <div className="text-[140px] font-black italic -rotate-12 mb-8 -ml-6 text-white">KNOW</div>
      </div>
      <div className="px-8 pt-20 flex justify-between items-start z-10 relative">
        <div className="flex flex-col pt-3">
          <h1 className="text-white text-[42px] font-black tracking-tight leading-none mb-1.5">Lovekey</h1>
          <p className="text-white/80 text-[13px] tracking-wide font-medium">一个可以帮你回复消息的键盘</p>
        </div>
        <div className="absolute top-4 right-[-30px]">
          <Screenshot3DKeycap />
        </div>
      </div>

      <div className="mx-6 mt-[70px] relative z-10">
        {setupStep === 1 && (
          <div className="w-full bg-white rounded-t-[20px] rounded-b-[20px] p-4 shadow-xl animate-pop-in relative overflow-hidden">
            <div className="w-full flex flex-col space-y-2.5">
              <div className="w-full h-[52px] bg-[#F7F8FA] rounded-[12px] flex items-center justify-between px-3">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-[#EAECEF] rounded-lg"></div>
                  <div className="w-24 h-4 bg-[#EAECEF] rounded-full"></div>
                </div>
                <div className="text-[#A0A5B5] text-[11px] font-bold bg-[#E6E8EC] px-3 py-1 rounded-full">未启用</div>
              </div>
              <div className="w-full h-[60px] bg-white border-[1.5px] border-[#EBF0F5] shadow-sm rounded-[12px] flex items-center justify-between px-3 relative">
                <div className="flex items-center space-x-3">
                  <AppIconBlue />
                  <span className="font-bold text-[#1A1A1A] text-[16px]">Lovekey</span>
                </div>
                <div className="text-[#A0A5B5] text-[11px] font-bold bg-[#F2F3F7] px-3 py-1 rounded-full">未启用</div>
              </div>
              <div className="w-full h-[52px] bg-[#F7F8FA] rounded-[12px] flex items-center justify-between px-3">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-[#EAECEF] rounded-lg"></div>
                  <div className="w-16 h-4 bg-[#EAECEF] rounded-full"></div>
                </div>
                <div className="text-[#A0A5B5] text-[11px] font-bold bg-[#E6E8EC] px-3 py-1 rounded-full">未启用</div>
              </div>
            </div>
            <div className="absolute inset-0 pointer-events-none z-20">
               <h3 className="absolute top-[115px] left-1/2 -translate-x-1/2 text-[30px] font-black text-stroke-black whitespace-nowrap -rotate-2">开启Lovekey</h3>
               <div className="absolute right-[15px] top-[75px] text-[34px] animate-click-hint drop-shadow-md">👆</div>
            </div>
          </div>
        )}

        {setupStep === 2 && (
          <div className="w-full bg-[#E3E4E8] rounded-[20px] p-4 shadow-xl animate-pop-in relative border border-white/50">
            <div className="flex justify-between items-center mb-3 px-1">
              <span className="text-[#888] text-[13px] font-bold">其他输入法</span>
              <ChevronUp size={16} className="text-[#888]" />
            </div>
            <div className="w-full h-[50px] bg-[#C6D2E8] rounded-[12px] flex items-center justify-between px-4 relative z-10 border border-white/40">
              <span className="font-bold text-[#0066FF] text-[15px]">Lovekey键盘</span>
              <Check size={20} strokeWidth={3} className="text-[#0066FF]" />
            </div>
            <div className="flex space-x-3 mt-5">
              <div className="flex-1 h-[38px] bg-[#CFD2DC] rounded-[10px] flex items-center justify-center text-[#888] text-[13px] font-bold">取消</div>
              <div className="flex-1 h-[38px] bg-[#486BFF] rounded-[10px] flex items-center justify-center text-white text-[13px] font-bold">确定</div>
            </div>
            <div className="absolute inset-0 pointer-events-none z-20">
               <h3 className="absolute top-[88px] left-1/2 -translate-x-1/2 text-[26px] font-black text-stroke-black whitespace-nowrap -rotate-2">选择Lovekey键盘</h3>
               <div className="absolute right-[25px] top-[45px] text-[34px] animate-click-hint drop-shadow-md">👆</div>
            </div>
          </div>
        )}
        <p className="mt-4 text-center text-white/80 text-[12px] font-medium tracking-wide">
          {setupStep === 1 ? '第1步：在「输入法」管理中，启用Lovekey' : '第2步：切换到Lovekey键盘'}
        </p>
      </div>

      <div className="mt-auto px-6 pb-12 flex flex-col gap-y-3 relative z-10">
        <button onClick={handleStep1Click} disabled={setupStep === 2} className={`w-full h-[56px] rounded-[14px] px-6 flex items-center justify-between transition-all duration-300 cursor-pointer ${setupStep === 1 ? 'bg-[#121212] text-white shadow-xl active:scale-[0.98]' : 'bg-white/10 text-white/40 cursor-not-allowed'}`}>
          <span className="text-[15px] font-bold tracking-wide">第一步 启用Lovekey键盘</span>
          {setupStep === 1 ? <ArrowRight size={18} strokeWidth={2.5}/> : <Check size={20} strokeWidth={3} className="text-white/40" />}
        </button>
        <button onClick={() => setupStep === 2 && setShowBottomSheet(true)} className={`w-full h-[56px] rounded-[14px] px-6 flex items-center justify-between transition-all duration-300 cursor-pointer ${setupStep === 2 ? 'bg-[#121212] text-white shadow-xl active:scale-[0.98] animate-pop-in' : 'bg-white/10 text-white/40'}`}>
          <span className="text-[15px] font-bold tracking-wide">第二步 切换到Lovekey键盘</span>
          <ArrowRight size={18} strokeWidth={2.5} className={setupStep === 2 ? 'text-white' : 'text-white/40'}/>
        </button>
      </div>

      {showBottomSheet && (
        <div className="absolute inset-0 z-50 flex flex-col justify-end animate-slide-up">
          <div className="absolute inset-0 bg-black/50 backdrop-blur-sm transition-opacity cursor-pointer" onClick={() => setShowBottomSheet(false)}></div>
          <div className="w-full bg-[#1C1C1E] rounded-t-[24px] relative z-10 pb-12 pt-6 px-6 shadow-[0_-10px_40px_rgba(0,0,0,0.5)] flex flex-col animate-slide-up">
             <h3 className="text-center text-white font-bold text-[16px] mb-8 tracking-wide">更改键盘</h3>
             <div className="flex items-center justify-between mb-7 px-2">
               <span className="text-white/90 text-[15px] tracking-wide">搜狗输入法定制版</span>
               <div className="w-[20px] h-[20px] rounded-full bg-[#0A84FF] flex items-center justify-center border-2 border-[#1C1C1E] ring-[1.5px] ring-[#0A84FF]">
                  <div className="w-[6px] h-[6px] bg-white rounded-full"></div>
               </div>
             </div>
             <div className="flex items-center justify-between px-2 cursor-pointer active:opacity-60 transition-opacity" onClick={() => { setShowBottomSheet(false); setTimeout(onNext, 400); }}>
               <span className="text-white font-bold text-[15px] tracking-wide flex items-center">
                 <span className="mr-2 text-[16px] text-[#FFD233]">👉</span> 
                 <span className="text-white">Lovekey键盘</span>
               </span>
               <div className="w-[20px] h-[20px] rounded-full border-[1.5px] border-[#48484A]"></div>
             </div>
          </div>
        </div>
      )}
    </div>
  );
};

const KeyboardTypeCard = ({ type, title, isNew, selected, onClick, children }) => (
  <div 
    onClick={() => onClick(type)}
    className={`w-full bg-white rounded-[20px] p-[18px] cursor-pointer transition-all duration-300 active:scale-[0.98] ${
      selected ? 'border-[2px] border-[#5C73FF] shadow-[0_8px_20px_rgba(92,115,255,0.15)]' : 'border-[2px] border-transparent shadow-[0_4px_16px_rgba(0,0,0,0.04)]'
    }`}
  >
    <div className="flex justify-between items-center mb-4">
      <div className="flex items-center">
        <span className={`text-[17px] font-bold ${selected ? 'text-[#5C73FF]' : 'text-[#1A1A1A]'}`}>{title}</span>
        {isNew && <span className="ml-2 px-1.5 py-0.5 bg-[#EEF2FF] text-[#5C73FF] text-[10px] font-black italic rounded-[4px]">New</span>}
      </div>
      <div className={`w-6 h-6 rounded-full flex items-center justify-center transition-colors duration-300 ${selected ? 'bg-[#5C73FF]' : 'border-[2px] border-[#E5E7EB]'}`}>
        {selected && <Check size={14} strokeWidth={4} className="text-white" />}
      </div>
    </div>
    {children}
  </div>
);

const KeyboardSelectScreen = ({ onNext }) => {
  const [selectedType, setSelectedType] = useState(null);

  return (
    <div className="absolute inset-0 bg-gradient-to-b from-[#EAEFFF] to-[#F4F6FC] flex flex-col">
      <div className="pt-[70px] pb-6 text-center shrink-0">
        <h2 className="text-[22px] font-extrabold text-[#1A1A1A] tracking-wide">选择你喜欢的中文键盘</h2>
      </div>
      <div className="flex-1 overflow-y-auto no-scrollbar px-5 pb-[120px] flex flex-col gap-4 relative z-10" style={{ WebkitOverflowScrolling: 'touch' }}>
        <KeyboardTypeCard type="nine" title="九宫格拼音" selected={selectedType === 'nine'} onClick={setSelectedType}><NineGridPreview /></KeyboardTypeCard>
        <KeyboardTypeCard type="full" title="全键盘拼音" selected={selectedType === 'full'} onClick={setSelectedType}><FullKeyboardPreview /></KeyboardTypeCard>
        <KeyboardTypeCard type="hand" title="手写键盘" isNew selected={selectedType === 'hand'} onClick={setSelectedType}><HandwritingPreview /></KeyboardTypeCard>
      </div>
      <div className="absolute bottom-0 w-full px-5 pb-8 pt-10 bg-gradient-to-t from-[#F4F6FC] via-[#F4F6FC] to-transparent pointer-events-none z-20">
        <button 
          onClick={onNext}
          disabled={!selectedType}
          className={`w-full h-[60px] rounded-[24px] flex items-center justify-center transition-all duration-300 pointer-events-auto cursor-pointer ${
            selectedType ? 'bg-[#121212] text-white shadow-[0_10px_20px_rgba(0,0,0,0.2)] active:scale-[0.98]' : 'bg-[#4B4B4B] text-white/80 opacity-90 cursor-not-allowed'
          }`}
        >
          <span className="text-[17px] font-bold tracking-wide">开始体验Lovekey键盘</span>
        </button>
      </div>
    </div>
  );
};

const ChatKeyboardScreen = ({ isTutorial, onComplete }) => {
  const [tutPhase, setTutPhase] = useState(isTutorial ? 0 : 5);
  const [showToast, setShowToast] = useState(false);

  const [messages, setMessages] = useState([
    { id: 1, type: 'bot', text: "👋 欢迎使用「Lovekey键盘」<br/>点击任一对话去粘贴，选择任意回复方式去试用吧~" },
    { id: 2, type: 'user', text: "在干嘛？" },
    { id: 3, type: 'user', text: "我去洗澡了" }
  ]);
  const [inputText, setInputText] = useState("");
  const [clipboardUsed, setClipboardUsed] = useState(false);

  const handleNextPhase = () => {
    if (!isTutorial) return;
    if (tutPhase === 0) {
      setShowToast(true);
      setTimeout(() => setShowToast(false), 2000);
      setTutPhase(1);
    } else if (tutPhase < 4) {
      setTutPhase(p => p + 1);
    }
  };

  const handleAppTagClick = (tagText) => {
    if (isTutorial) return;
    const mockReplies = {
      "高情商": "在呼吸，在心跳，在想你呀~",
      "心动狙击": "在想怎么回复才能让你心动💓",
      "幽默": "在思考宇宙的终极奥秘...顺便想你",
      "暖男": "刚忙完，正准备找你呢，你今天累不累？",
      "暧昧拉扯": "你猜猜看？猜对有奖哦~",
      "情场高手": "本来在发呆，看到你的消息心跳就漏了一拍",
      "温柔大叔": "乖，别闹，正想着带你去吃什么好吃的",
      "风流浪子": "在看手机，等一个漂亮女孩的消息，哎，这不就来了嘛",
      "幽默有梗": "在进行光合作用，维持生命体征"
    };
    setInputText(mockReplies[tagText] || `[${tagText}] 生成的回复...`);
  };

  const handleAppSend = () => {
    if (isTutorial || !inputText) return;
    setMessages(prev => [...prev, { id: Date.now(), type: 'me', text: inputText }]);
    setInputText("");
    setClipboardUsed(true);
  };

  return (
    <div className="absolute inset-0 bg-[#F2F2F7] flex flex-col overflow-hidden">
      <div className={`h-[60px] flex justify-between items-center px-4 shrink-0 relative z-10 ${!isTutorial ? 'bg-[#F2F2F7] border-b border-[#E5E5EA]' : 'bg-[#F2F2F7]'}`}>
        <button onClick={() => !isTutorial && onComplete()} className="w-8 h-8 flex items-center justify-center bg-white rounded-full shadow-sm cursor-pointer active:scale-95 transition-transform"><ChevronLeft size={20}/></button>
        {isTutorial ? (
          <button className="text-[#888] text-[14px] font-medium flex items-center bg-transparent border-none cursor-pointer" onClick={onComplete}>
            我会用，跳过 <ArrowRight size={14} className="ml-0.5"/>
          </button>
        ) : (
          <span className="font-bold text-[#1A1A1A]">日常聊天</span>
        )}
        {!isTutorial && <div className="w-8"></div>}
      </div>

      <div className="flex-1 px-4 py-2 flex flex-col gap-4 overflow-y-auto no-scrollbar relative z-10">
        {isTutorial && <LocalBackdrop active={tutPhase < 4} onClick={handleNextPhase} />}
        
        {isTutorial ? (
          <>
            <div className="flex items-start gap-2 relative z-[40]">
              <div className="w-10 h-10 rounded-full bg-[#E5E9FF] flex items-center justify-center shrink-0 border border-[#D0D9FF]"><span className="text-[20px]">😊</span></div>
              <div className="bg-white p-3.5 rounded-2xl rounded-tl-sm shadow-sm text-[#333] text-[14px] leading-relaxed max-w-[240px]">
                <span className="text-[16px]">👋</span> 欢迎使用「Lovekey键盘」<br/>点击任一对话去粘贴，选择任意回复方式去试用吧~
              </div>
            </div>

            <div className="flex items-start gap-2 pl-12 relative">
              <div className={`w-[240px] bg-white p-3.5 rounded-2xl shadow-sm text-[#333] text-[15px] flex justify-between items-center transition-all duration-300 ${isTutorial && tutPhase === 0 ? '!z-[60] shadow-[0_0_0_2px_#3B5BFF] cursor-pointer relative' : 'z-[40] relative'}`} onClick={() => isTutorial && tutPhase === 0 && handleNextPhase()}>
                <span>在干嘛？</span>
                <div className="text-[#888]"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg></div>
                {tutPhase === 0 && (
                  <div className="absolute -top-[140px] right-[-30px] z-[70] pointer-events-none">
                     <BrushStrokeBg className="px-6 py-3 w-[260px] h-[70px] absolute -bottom-[70px] right-[40px]">
                       <span className="text-[20px]">先点这里<br/>复制对方的话</span>
                     </BrushStrokeBg>
                     <div className="absolute top-[20px] right-0 flex flex-col items-center">
                       <div className="text-[60px] drop-shadow-xl z-20 relative transform -rotate-12" style={{ animation: 'hand-point-down 1.5s ease-in-out infinite' }}>👇</div>
                       <div className="absolute top-[60px] -right-[15px] z-10 transform -rotate-12"><CurvedArrow type="down" color="#FFD233" /></div>
                     </div>
                  </div>
                )}
              </div>
            </div>

            <div className="flex items-start gap-2 pl-12 relative z-[40]">
              <div className="w-[240px] bg-white p-3.5 rounded-2xl shadow-sm text-[#333] text-[15px] flex justify-between items-center">
                <span>我去洗澡了</span>
                <div className="text-[#888]"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg></div>
              </div>
            </div>

            {tutPhase >= 3 && (
              <div className="flex justify-end mt-2 mb-2 relative z-[40]">
                <div className="bg-[#5C73FF] text-white p-3.5 rounded-2xl rounded-tr-sm shadow-sm text-[15px] max-w-[240px] animate-pop-in">如果我说我在等你的回复呢</div>
              </div>
            )}
            {tutPhase >= 3 && (
              <div className="flex items-start gap-2 animate-slide-up relative z-[40]">
                <div className="w-10 h-10 rounded-full bg-[#E5E9FF] flex items-center justify-center shrink-0 border border-[#D0D9FF]"><span className="text-[20px]">😊</span></div>
                <div className="bg-white p-3.5 rounded-2xl rounded-tl-sm shadow-sm text-[#333] text-[14px] leading-relaxed max-w-[240px]">
                  🎉如您遇到其他问题，可点击<span className="text-[#5C73FF]">在线客服</span>，帮您解决
                </div>
              </div>
            )}
          </>
        ) : (
          messages.map(msg => {
            if (msg.type === 'bot') {
               return (
                 <div key={msg.id} className="flex items-start gap-2">
                   <div className="w-10 h-10 rounded-full bg-[#E5E9FF] flex items-center justify-center shrink-0 border border-[#D0D9FF]"><span className="text-[20px]">😊</span></div>
                   <div className="bg-white p-3.5 rounded-2xl rounded-tl-sm shadow-sm text-[#333] text-[14px] leading-relaxed max-w-[240px]" dangerouslySetInnerHTML={{__html: msg.text}} />
                 </div>
               );
            } else if (msg.type === 'me') {
               return (
                 <div key={msg.id} className="flex justify-end mt-2 mb-2 animate-pop-in">
                   <div className="bg-[#5C73FF] text-white p-3.5 rounded-2xl rounded-tr-sm shadow-sm text-[15px] max-w-[240px]">{msg.text}</div>
                 </div>
               );
            } else {
               return (
                 <div key={msg.id} className="flex items-start gap-2 pl-12 cursor-pointer active:opacity-60" onClick={() => !clipboardUsed && setInputText(msg.text)}>
                   <div className="w-[240px] bg-white p-3.5 rounded-2xl shadow-sm text-[#333] text-[15px] flex justify-between items-center">
                     <span>{msg.text}</span>
                     <div className="text-[#888]"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg></div>
                   </div>
                 </div>
               );
            }
          })
        )}
        <div className="h-[20px]"></div>
      </div>

      {showToast && (
        <div className="absolute top-[40%] left-1/2 -translate-x-1/2 z-[70] bg-black/60 backdrop-blur-md text-white px-6 py-2.5 rounded-full text-[14px] pointer-events-none" style={{ animation: 'toast-fade 2s ease-in-out forwards' }}>复制成功</div>
      )}

      {isTutorial ? (
        <div className="w-full bg-[#F2F2F7] flex flex-col shrink-0 pb-6 relative shadow-[0_-5px_15px_rgba(0,0,0,0.05)] border-t border-[#E5E5EA] z-10">
          <LocalBackdrop active={tutPhase < 4} onClick={handleNextPhase} />
          
          <div className="h-[40px] flex items-center px-4 bg-white m-2 rounded-xl border border-[#E5E5EA] relative z-[40]">
            {tutPhase >= 2 ? <span className="text-[#333] text-[14px]">如果我说我在等你的回复呢</span> : <span className="text-[#A0A5B5] text-[13px]">👇在键盘粘贴问题后，选择回复方式</span>}
          </div>

          <div className="flex items-center px-3 py-1 mb-1 gap-1.5 relative">
            <div className={`w-9 h-9 flex items-center justify-center rounded-full transition-all ${tutPhase === 3 ? 'bg-white !z-[60] shadow-[0_0_0_2px_#3B5BFF] cursor-pointer' : 'bg-[#EAECEF] text-[#5C73FF] z-[40]'} relative`} onClick={() => tutPhase === 3 && handleNextPhase()}>
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="2" y="4" width="20" height="16" rx="2" ry="2"></rect><path d="M6 8h.01M10 8h.01M14 8h.01M18 8h.01M8 12h.01M12 12h.01M16 12h.01M7 16h10"></path></svg>
              {tutPhase === 3 && (
                <div className="absolute -top-[160px] left-[30px] z-[70] pointer-events-none">
                   <BrushStrokeBg className="px-6 py-3 w-[260px] h-[70px]"><span className="text-[20px]">这里还有<br/>日常的输入</span></BrushStrokeBg>
                   <div className="absolute -bottom-[35px] left-[-60px] flex items-center"><div className="text-[50px] drop-shadow-xl transform scale-x-[-1] rotate-[70deg]" style={{ animation: 'hand-point-down 1.5s ease-in-out infinite' }}>👆</div></div>
                   <div className="absolute -bottom-[10px] left-[-30px]"><CurvedArrow type="up-left" color="#4B66FF" /></div>
                </div>
              )}
            </div>
            <div className="px-3.5 h-8 bg-[#4B66FF] text-white rounded-full flex items-center justify-center text-[13px] font-bold relative z-[40] shadow-sm">帮你回</div>
            <div className="px-3.5 h-8 bg-white text-[#555] rounded-full flex items-center justify-center text-[13px] font-bold relative z-[40] shadow-sm">超会说</div>
            <div className="ml-auto w-8 h-8 rounded-full flex items-center justify-center text-[#888] bg-transparent border border-[#D3D8E6] relative z-[40]"><span className="text-[12px] font-bold font-serif">Hi</span></div>
            <div className="w-8 h-8 rounded-full flex items-center justify-center text-[#888] bg-transparent border border-[#D3D8E6] relative z-[40]"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="7" height="7" rx="1"></rect><rect x="14" y="3" width="7" height="7" rx="1"></rect><rect x="14" y="14" width="7" height="7" rx="1"></rect><rect x="3" y="14" width="7" height="7" rx="1"></rect></svg></div>
            <div className="relative z-[40] ml-0.5"><HeartPercentBadge percent="100%" active={true} /></div>
          </div>

          <div className="px-2 mb-2 flex gap-2 relative z-[40]">
            <div className="flex-1 bg-[#EAECEF] h-[48px] rounded-xl flex items-center justify-center text-[#555] font-bold text-[15px]">{tutPhase >= 1 ? '在干嘛？' : '+ 请粘贴TA的话'}</div>
            <div className="w-[60px] h-[48px] bg-[#4B66FF] rounded-xl flex items-center justify-center text-white text-[14px] font-bold shadow-sm opacity-80 cursor-pointer hover:opacity-100">粘贴</div>
          </div>

          <div className="px-2 grid grid-cols-4 gap-2 relative">
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">情绪价值</div>
            <div className={`h-[44px] rounded-xl flex items-center justify-center text-[13px] font-bold shadow-sm transition-all ${tutPhase === 1 ? 'bg-white !z-[60] shadow-[0_0_0_2px_#3B5BFF] cursor-pointer' : 'bg-white text-[#555] z-[40] cursor-pointer hover:shadow-md'} relative`} onClick={() => tutPhase === 1 && handleNextPhase()}>
              😊 幽默
              {tutPhase === 1 && (
                <div className="absolute top-[20px] right-[-200px] z-[70] pointer-events-none">
                   <BrushStrokeBg className="px-6 py-3 w-[260px] h-[60px] absolute -bottom-[60px] right-[-5px]"><span className="text-[20px]">试试幽默的回复吧</span></BrushStrokeBg>
                   <div className="absolute -top-[10px] left-[40px] flex items-center"><div className="text-[60px] drop-shadow-xl transform -scale-x-100" style={{ animation: 'hand-point-left 1.5s ease-in-out infinite' }}>👉</div></div>
                   <div className="absolute top-[0px] left-[105px]"><CurvedArrow type="left-up" color="#FFD233" /></div>
                </div>
              )}
            </div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#C1C8DF]"><KeyIconBackspace /></div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">🌞 暖男</div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">🔥 热烈深情</div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">👑 成熟</div>
            <div className="h-[44px] bg-[#D3D8E6] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#C1C8DF]">清空</div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">👏 夸夸</div>
            <div className="h-[44px] bg-[#EAECEF] rounded-xl flex items-center justify-center text-[13px] text-[#555] relative z-[40] cursor-pointer hover:bg-[#D3D8E6]">🕶️ 情场高手</div>
            <div className="h-[44px] bg-white rounded-xl flex items-center justify-center text-[#555] font-bold shadow-sm relative z-[40] cursor-pointer hover:bg-gray-50">+</div>
            <div className={`h-[44px] rounded-xl flex items-center justify-center font-bold shadow-sm transition-all ${tutPhase === 2 ? 'bg-[#4B66FF] text-white text-[14px] !z-[60] shadow-[0_0_0_2px_#3B5BFF] scale-105 cursor-pointer' : 'bg-[#4B66FF] text-white text-[14px] z-[40] cursor-pointer hover:bg-[#3A55EA]'} relative`} onClick={() => tutPhase === 2 && handleNextPhase()}>
              发送
              {tutPhase === 2 && (
                <div className="absolute bottom-[20px] right-[50px] z-[70] pointer-events-none">
                   <BrushStrokeBg className="px-6 py-3 w-[200px] h-[60px] absolute -top-[50px] right-[70px]"><span className="text-[22px]">发送给他吧~</span></BrushStrokeBg>
                   <div className="absolute top-[0px] left-[5px] flex items-center"><div className="text-[60px] drop-shadow-xl" style={{ animation: 'hand-point-right 1.5s ease-in-out infinite' }}>👉</div></div>
                   <div className="absolute -top-[10px] left-[85px] transform rotate-[-20deg]"><CurvedArrow type="right" color="#4B66FF" /></div>
                </div>
              )}
            </div>
          </div>
        </div>
      ) : (
        <div className="w-full bg-[#2A2A35] flex flex-col shrink-0 pb-6 shadow-[0_-10px_30px_rgba(0,0,0,0.2)]">
          <div className="bg-[#E5E5EA] px-3 py-2 shrink-0">
            <div className="bg-white rounded-xl h-[40px] flex items-center px-3 shadow-inner">
              {inputText ? <span className="text-[#333] text-[15px]">{inputText}</span> : <><span className="text-[16px] mr-1.5">👇</span><span className="text-[#A0A5B5] text-[13px]">在键盘粘贴问题后，选择回复方式</span></>}
            </div>
          </div>
          <div className="flex items-center px-3 py-2.5 gap-1.5">
            <div className="w-9 h-9 flex items-center justify-center rounded-full bg-[#414259] text-[#E0E0E0] cursor-pointer active:scale-95 transition-transform"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="2" y="4" width="20" height="16" rx="2" ry="2"></rect><path d="M6 8h.01M10 8h.01M14 8h.01M18 8h.01M8 12h.01M12 12h.01M16 12h.01M7 16h10"></path></svg></div>
            <div className="px-3.5 h-8 bg-[#4B66FF] text-white rounded-full flex items-center justify-center text-[13px] font-bold shadow-sm cursor-pointer active:scale-95 transition-transform">帮你回</div>
            <div className="px-3.5 h-8 bg-[#414259] text-[#E0E0E0] rounded-full flex items-center justify-center text-[13px] font-bold cursor-pointer active:scale-95 transition-transform">超会说</div>
            <div className="ml-auto w-8 h-8 rounded-full flex items-center justify-center text-[#E0E0E0] bg-transparent border border-[#50516A] cursor-pointer active:scale-95 transition-transform"><span className="text-[12px] font-bold font-serif">Hi</span></div>
            <div className="w-8 h-8 rounded-full flex items-center justify-center text-[#E0E0E0] bg-transparent border border-[#50516A] cursor-pointer active:scale-95 transition-transform"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><rect x="3" y="3" width="7" height="7" rx="1"></rect><rect x="14" y="3" width="7" height="7" rx="1"></rect><rect x="14" y="14" width="7" height="7" rx="1"></rect><rect x="3" y="14" width="7" height="7" rx="1"></rect></svg></div>
            <div className="ml-0.5"><HeartPercentBadge percent="30%" active={false} /></div>
          </div>
          <div className="px-2.5 mb-2.5 flex gap-2">
            <div onClick={() => !clipboardUsed && setInputText("在干嘛？")} className="flex-1 bg-[#404153] h-[52px] rounded-[14px] flex items-center justify-center text-[#6E8BFF] font-bold text-[16px] cursor-pointer active:scale-95 transition-transform">{clipboardUsed ? '' : '在干嘛？'}</div>
            <div onClick={() => !clipboardUsed && setInputText("在干嘛？")} className="w-[66px] h-[52px] bg-[#4B66FF] rounded-[14px] flex items-center justify-center text-white text-[15px] font-bold shadow-sm cursor-pointer active:scale-95 transition-transform">粘贴</div>
          </div>
          <div className="px-2.5 grid grid-cols-4 gap-2">
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("高情商")}>🍬 高情商</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("心动狙击")}>😘 心动狙击</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("幽默")}>😆 幽默</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[#E0E0E0] cursor-pointer active:scale-95 transition-transform" onClick={() => setInputText("")}><KeyIconBackspace /></div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("暖男")}>🌞 暖男</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("暧昧拉扯")}>💋 暧昧拉扯</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("情场高手")}>🎀 情场高手</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => setInputText("")}>清空</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("温柔大叔")}>👨‍⚖️ 温柔大叔</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("风流浪子")}>🎁 风流浪子</div>
            <div className="h-[46px] bg-[#404153] rounded-[12px] flex items-center justify-center text-[13.5px] text-[#E0E0E0] font-medium cursor-pointer active:scale-95 transition-transform" onClick={() => handleAppTagClick("幽默有梗")}>😋 幽默有梗</div>
            <div className={`h-[46px] rounded-[12px] flex items-center justify-center font-bold shadow-sm transition-transform cursor-pointer active:scale-95 ${inputText ? 'bg-[#4B66FF] text-white' : 'bg-[#404153] text-[#E0E0E0]'}`} onClick={handleAppSend}>发送</div>
          </div>
        </div>
      )}

      {isTutorial && tutPhase === 4 && (
        <div className="absolute inset-0 z-[80] bg-black/60 flex flex-col items-center justify-center animate-fade-in px-8">
           <div className="absolute inset-0 overflow-hidden pointer-events-none">
             {[...Array(30)].map((_, i) => (
                <div key={i} className="absolute w-2 h-6 rounded-full opacity-80" style={{ backgroundColor: ['#FF6B6B', '#4ECDC4', '#FFD93D', '#6B5B95'][i%4], top: `${Math.random() * 100}%`, left: `${Math.random() * 100}%`, transform: `rotate(${Math.random() * 360}deg) scale(${0.5 + Math.random()})`}} />
             ))}
           </div>
           <div className="text-[120px] drop-shadow-2xl mb-2" style={{ animation: 'celebration-pop 0.8s cubic-bezier(0.34, 1.56, 0.64, 1)' }}>👏</div>
           <h2 className="text-white text-[42px] font-black italic tracking-wider text-stroke-black mb-12 text-center" style={{ textShadow: '0 8px 16px rgba(0,0,0,0.5)', animation: 'celebration-pop 1s cubic-bezier(0.34, 1.56, 0.64, 1)' }}>哇！你好棒呀</h2>
           <button onClick={onComplete} className="w-full h-[64px] bg-[#5C73FF] rounded-[20px] text-white text-[18px] font-bold shadow-[0_10px_20px_rgba(92,115,255,0.4)] active:scale-95 transition-transform cursor-pointer">去使用吧</button>
        </div>
      )}
    </div>
  );
};


// ==========================================
// 支付墙体系 (Step 8 & 弹窗)
// ==========================================
const PaywallMainScreen = ({ onNext, onDownsell }) => {
  const [timeLeft, setTimeLeft] = useState(11 * 3600 + 59 * 58);
  const [selectedPlan, setSelectedPlan] = useState('lifetime');

  useEffect(() => {
    const timer = setInterval(() => setTimeLeft(prev => (prev > 0 ? prev - 1 : 0)), 1000);
    return () => clearInterval(timer);
  }, []);

  const formatTime = (seconds) => {
    const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
    const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${h}:${m}:${s}`;
  };

  return (
    <div className="absolute inset-0 bg-gradient-to-b from-[#FFA77A] via-[#FFF8F0] to-[#FFF8F0] flex flex-col overflow-hidden">
      <div className="absolute top-10 left-5 z-50">
        <button onClick={onDownsell} className="w-8 h-8 rounded-full bg-white/40 backdrop-blur-md flex items-center justify-center text-white shadow-sm cursor-pointer"><X size={20} strokeWidth={2.5} /></button>
      </div>

      <div className="w-full flex-1 min-h-0 overflow-y-auto no-scrollbar relative z-10">
        <div className="flex flex-col w-full min-h-max pb-[220px]">
          <div className="w-full pt-16 flex flex-col items-center relative">
            <div className="absolute top-12 left-8 text-white/40">✨</div>
            <div className="absolute top-8 right-12 text-white/40">✨</div>
            
            <div className="flex items-center space-x-1 mb-2">
              <span className="bg-white/30 text-white rounded-full px-2 py-0.5 text-[10px] font-bold border border-white/40 transform -rotate-12">00</span>
              <h1 className="text-[44px] font-black italic text-stroke-red transform -rotate-6 animate-pulse-soft">限时</h1>
            </div>
            
            <div className="flex items-center space-x-2 z-10 mt-[-10px]">
              <span className="text-[60px] drop-shadow-xl z-20 -mr-6">🎠</span>
              <div className="bg-white rounded-full px-6 py-2 shadow-xl border-[4px] border-[#E02020] transform rotate-3 flex items-center">
                <h2 className="text-[#E02020] text-[40px] font-black italic tracking-tighter leading-none flex items-baseline">立减<span className="text-[58px]">90</span><span className="text-[24px]">元</span></h2>
              </div>
            </div>
            
            <div className="mt-4 bg-gradient-to-r from-[#FFD233] to-[#FFB700] text-[#8B3A00] px-5 py-1.5 rounded-full font-black text-[15px] shadow-md border border-white/50 flex items-center space-x-1 transform -rotate-2">
              <span>✨</span><span>L+ 永久会员</span>
            </div>
          </div>

          <div className="absolute top-12 right-0 pointer-events-none opacity-20"><div className="w-[200px] h-[200px] bg-[#FF5B5B] rounded-full filter blur-[60px]"></div></div>

          <div className="mt-10 px-8 flex flex-col gap-y-4">
            <div className="flex items-center space-x-3 text-[#A93B22]">
              <div className="w-5 h-5 rounded border-[1.5px] border-current flex flex-col items-center pt-0.5"><div className="w-2.5 h-[1.5px] bg-current rounded-full mb-[2px]"></div><div className="w-1.5 h-[1.5px] bg-current rounded-full"></div></div>
              <span className="text-[15px] font-bold tracking-wide">键盘实时帮回复，不限次</span>
            </div>
            <div className="flex items-center space-x-3 text-[#A93B22]"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="10"></circle><path d="M8 14s1.5 2 4 2 4-2 4-2"></path><line x1="9" y1="9" x2="9.01" y2="9"></line><line x1="15" y1="9" x2="15.01" y2="9"></line></svg><span className="text-[15px] font-bold tracking-wide">幽默、高情商...海量人设，免费使用</span></div>
            <div className="flex items-center space-x-3 text-[#A93B22]"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"></path></svg><span className="text-[15px] font-bold tracking-wide">会员专享，定制专属人设</span></div>
            <div className="flex items-center space-x-3 text-[#A93B22]"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"></path></svg><span className="text-[15px] font-bold tracking-wide">亲密度调节，自动把控聊天分寸</span></div>
            <div className="flex items-center space-x-3 text-[#A93B22]"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg><span className="text-[15px] font-bold tracking-wide">会员优先体验新聊天能力</span></div>
          </div>

          <div className="mt-8 px-6 flex gap-3">
            <div onClick={() => setSelectedPlan('lifetime')} className={`flex-1 rounded-[24px] p-4 pt-6 pb-8 relative cursor-pointer transition-all ${selectedPlan === 'lifetime' ? 'bg-white border-2 border-[#FF3B30] shadow-[0_8px_20px_rgba(255,59,48,0.2)]' : 'bg-white/60 border-2 border-transparent'}`}>
              <div className="absolute -top-[2px] -left-[2px] bg-[#FF3B30] text-white px-2.5 py-1 rounded-br-xl rounded-tl-[20px] flex items-center space-x-1 shadow-sm z-10"><svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg><span className="text-[10px] font-bold">活动倒计时 {formatTime(timeLeft)}</span></div>
              <h3 className="text-[16px] font-bold text-[#1A1A1A] mt-2">永久会员</h3>
              <p className="text-[11px] text-[#888] mt-0.5 font-medium">一次购买，终身免费</p>
              <div className="mt-4 text-[#FF3B30] font-bold flex items-baseline"><span className="text-[14px]">￥</span><span className="text-[36px] leading-none tracking-tight">128</span></div>
              <p className="text-[#D0A678] text-[11px] font-bold mt-1">低至 ￥<span className="text-[14px]">1</span>/月</p>
              <div className="absolute -bottom-3 left-1/2 -translate-x-1/2 bg-[#FF3B30] text-white px-4 py-1.5 rounded-full text-[12px] font-bold shadow-md whitespace-nowrap">立减 ￥90</div>
            </div>

            <div onClick={() => setSelectedPlan('monthly')} className={`flex-[0.8] rounded-[24px] p-4 pt-6 cursor-pointer transition-all flex flex-col ${selectedPlan === 'monthly' ? 'bg-white border-2 border-[#FF3B30] shadow-[0_8px_20px_rgba(255,59,48,0.2)]' : 'bg-white/60 border-2 border-white/50'}`}>
              <h3 className="text-[15px] font-bold text-[#1A1A1A] mt-2">月度会员</h3>
              <div className="mt-auto mb-2 text-[#1A1A1A] font-bold flex items-baseline"><span className="text-[12px]">￥</span><span className="text-[28px] leading-none tracking-tight">48</span></div>
            </div>
          </div>
        </div>
      </div>

      <div className="absolute bottom-0 w-full px-6 pb-8 pt-12 bg-gradient-to-t from-[#FFF8F0] via-[#FFF8F0] to-transparent flex flex-col z-20 pointer-events-none">
        <div className="bg-white rounded-[16px] p-3.5 flex items-center justify-between mb-4 pointer-events-auto cursor-pointer shadow-sm border border-[#F2E8E0]">
          <div className="flex items-center space-x-2"><div className="w-[22px] h-[22px] bg-[#1677FF] rounded-[4px] flex items-center justify-center"><span className="text-white text-[12px] font-bold">支</span></div><span className="text-[15px] font-bold text-[#1A1A1A]">支付宝</span></div>
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#A0A5B5" strokeWidth="2"><path d="M17 8l4 4m0 0l-4 4m4-4H3" /><path d="M7 16l-4-4m0 0l4-4m-4 4h18" /></svg>
        </div>
        <button onClick={onNext} className="w-full h-[60px] bg-[#D62020] rounded-full flex items-center justify-center shadow-[0_10px_20px_rgba(214,32,32,0.3)] active:scale-[0.98] transition-transform cursor-pointer pointer-events-auto"><span className="text-white text-[18px] font-bold tracking-widest">立即解锁</span></button>
        <p className="text-center text-[11px] text-[#A0A5B5] mt-4 font-medium pointer-events-auto cursor-pointer">点击支付即表示已阅读并同意《会员协议》</p>
      </div>
    </div>
  );
};

const DownsellModal = ({ onClose, onNext }) => {
  const [timeLeft, setTimeLeft] = useState(59 * 60 + 58);
  useEffect(() => {
    const timer = setInterval(() => setTimeLeft(prev => (prev > 0 ? prev - 1 : 0)), 1000);
    return () => clearInterval(timer);
  }, []);

  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${m}:${s}`;
  };

  return (
    <div className="absolute inset-0 z-[100] bg-black/60 backdrop-blur-sm flex flex-col justify-end animate-fade-in">
      <div className="absolute inset-0 cursor-pointer" onClick={onClose}></div>
      <div className="w-full h-[80%] bg-[#0A0A12] rounded-t-[32px] relative overflow-hidden flex flex-col animate-slide-up">
        <div className="absolute top-5 right-5 z-20"><button onClick={onClose} className="w-8 h-8 rounded-lg bg-white/10 flex items-center justify-center text-white/60 cursor-pointer"><X size={20} strokeWidth={2.5} /></button></div>
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-full h-[300px] bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-[#4A3B7A] via-[#15112A] to-[#0A0A12] opacity-80 pointer-events-none"></div>

        <div className="flex-1 flex flex-col items-center pt-16 px-6 relative z-10">
          <h2 className="text-[32px] font-black italic bg-gradient-to-r from-[#B4C5FF] to-[#E8EEFF] bg-clip-text text-transparent mb-1">限时</h2>
          <h1 className="text-[46px] font-black text-white tracking-tight mb-4">买一月送一月</h1>
          <p className="text-white/80 text-[15px] font-medium tracking-wide">解锁键盘全部功能不限次</p>
          <div className="mt-12 flex flex-col items-center"><span className="text-[#888] text-[12px] mb-2">距活动结束</span><div className="bg-[#2A2A35] px-6 py-2 rounded-full border border-white/5"><span className="text-white font-mono text-[22px] font-bold tracking-widest">{formatTime(timeLeft)}</span></div></div>
          <div className="w-full mt-10 bg-[#1D1D28] rounded-[20px] p-5 border border-white/10 shadow-xl">
            <div className="flex justify-between items-start">
              <div><h3 className="text-white text-[16px] font-bold mb-1">特惠月卡买一送一</h3><p className="text-[#888] text-[13px]">买一月送一月</p></div>
              <div className="bg-[#FF4B6B] text-white px-3 py-1 rounded-full text-[12px] font-bold">新用户优惠</div>
            </div>
            <div className="flex justify-between items-end mt-6">
              <span className="text-[#555] text-[16px] line-through font-bold">￥96</span>
              <div className="text-[#FF7A92] font-bold"><span className="text-[13px]">优惠后 </span><span className="text-[18px]">￥</span><span className="text-[26px]">24</span><span className="text-[13px]">/月</span></div>
            </div>
          </div>
        </div>
        <div className="mt-auto px-6 pb-12 pt-4 relative z-10"><button onClick={onNext} className="w-full h-[60px] bg-gradient-to-r from-[#6E7BFF] to-[#8C64FF] rounded-full flex items-center justify-center text-white text-[18px] font-bold tracking-widest shadow-[0_8px_20px_rgba(110,123,255,0.4)] active:scale-95 transition-transform cursor-pointer">领取优惠</button></div>
      </div>
    </div>
  );
};

// ==========================================
// 模态弹窗组件 (AppHomeScreen 使用)
// ==========================================
const LoginModal = ({ onClose }) => (
  <div className="absolute inset-0 z-[200] bg-black/40 backdrop-blur-[2px] flex items-center justify-center animate-fade-in" onClick={onClose}>
    <div className="w-[85%] bg-white rounded-[28px] p-8 pt-10 relative flex flex-col items-center shadow-2xl animate-pop-in" onClick={e => e.stopPropagation()}>
      <button onClick={onClose} className="absolute top-5 right-5 text-[#333] cursor-pointer hover:bg-gray-100 p-1 rounded-full"><X size={20} strokeWidth={2.5} /></button>
      <div className="w-full text-left mb-8"><h2 className="text-[24px] font-black text-[#1A1A1A] leading-tight tracking-wide">登录LOVEKEY<br/>添加聊天人设到键盘</h2></div>
      <button className="w-full h-[52px] bg-[#5C73FF] rounded-full flex items-center justify-center text-white text-[16px] font-bold shadow-[0_6px_16px_rgba(92,115,255,0.3)] active:scale-95 transition-transform cursor-pointer mb-8 tracking-wider"><Smartphone size={20} className="mr-2" strokeWidth={2.5} /> 手机号登录</button>
      <div className="flex flex-col items-center mb-10"><WeChatIcon /><span className="text-[#999] text-[12px] mt-2 font-medium">微信</span></div>
      <div className="flex items-start text-[11px] text-[#999] w-full px-2">
        <div className="w-[14px] h-[14px] rounded-full border-[1.5px] border-[#CCC] mr-2 mt-[2px] flex-shrink-0 cursor-pointer"></div>
        <p className="leading-relaxed">我已阅读并同意 <span className="text-[#333]">用户协议</span>、<span className="text-[#333]">用户隐私协议</span>和<span className="text-[#333]">号码认证服务隐私协议</span></p>
      </div>
    </div>
  </div>
);

const KeyboardSwitchModal = ({ onClose }) => (
  <div className="absolute inset-0 z-[200] bg-black/50 backdrop-blur-[2px] flex flex-col justify-end animate-fade-in" onClick={onClose}>
    <div className="w-full bg-[#2C2C2C] rounded-t-[24px] p-6 pb-12 shadow-[0_-10px_40px_rgba(0,0,0,0.5)] animate-slide-up" onClick={e => e.stopPropagation()}>
      <h3 className="text-center text-white/90 font-bold text-[17px] mb-8 tracking-wide">更改键盘</h3>
      <div className="flex items-center justify-between mb-8 px-2 cursor-pointer opacity-70">
        <span className="text-white/90 text-[16px] tracking-wide">搜狗输入法定制版</span><div className="w-[22px] h-[22px] rounded-full border-[2px] border-gray-500"></div>
      </div>
      <div className="flex items-center justify-between px-2 cursor-pointer" onClick={onClose}>
        <span className="text-white font-bold text-[16px] tracking-wide flex items-center"><span className="mr-2 text-[18px] text-[#FFD233]">👉</span><span>Lovekey键盘</span></span>
        <div className="w-[22px] h-[22px] rounded-full bg-[#5C73FF] flex items-center justify-center"><div className="w-[8px] h-[8px] bg-white rounded-full"></div></div>
      </div>
    </div>
  </div>
);

// ==========================================
// 主干底栏容器及首页 (Step 10)
// ==========================================
const AppHomeScreen = ({ onShowLogin, onShowKeyboard }) => {
  const [timeLeft, setTimeLeft] = useState(11 * 3600 + 59 * 45); 

  useEffect(() => {
    const timer = setInterval(() => setTimeLeft(prev => (prev > 0 ? prev - 1 : 0)), 1000);
    return () => clearInterval(timer);
  }, []);

  const formatTime = (seconds) => {
    const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
    const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
    const s = (seconds % 60).toString().padStart(2, '0');
    return `${h}:${m}:${s}.8`; 
  };

  return (
    <div className="absolute inset-0 bg-[#F4F5FB] flex flex-col overflow-hidden">
      <div className="absolute top-0 w-full h-[550px] bg-gradient-to-b from-[#E2E6FF] via-[#EEF0FA] to-[#F4F5FB] pointer-events-none"></div>

      <div className="h-[60px] flex justify-end items-center px-5 relative z-20 mt-2">
        <div className="flex items-center gap-3">
          <div className="bg-gradient-to-r from-[#FF6B22] to-[#FF4500] px-3.5 py-1.5 rounded-full text-white text-[13px] font-bold flex items-center shadow-md">
            <span className="bg-[#FFE066] text-[#FF4500] text-[10px] px-1 rounded-[4px] mr-1.5 leading-tight">L+</span> 立减 90
          </div>
          <div className="w-[36px] h-[36px] rounded-full bg-white shadow-sm border border-gray-100 flex items-center justify-center cursor-pointer active:scale-95" onClick={onShowLogin}>
            <User size={18} className="text-[#333]" strokeWidth={2.5} />
          </div>
        </div>
      </div>

      <div className="flex-1 min-h-0 overflow-y-auto no-scrollbar relative z-10" style={{ WebkitOverflowScrolling: 'touch' }}>
        <div className="flex flex-col pb-[120px]">
          
          <div className="flex flex-col items-center pt-2 pb-10 relative">
            <div className="relative bg-[#B3CAFF] text-white px-3.5 py-1.5 rounded-lg text-[13px] font-bold z-10 mb-1 tracking-wide">推荐亲密度<div className="absolute -bottom-[5px] left-1/2 -translate-x-1/2 w-0 h-0 border-l-[6px] border-r-[6px] border-t-[6px] border-l-transparent border-r-transparent border-t-[#B3CAFF]"></div></div>
            <h1 className="num-font text-[80px] font-black text-[#1A1A1A] tracking-tighter leading-none z-10 flex items-baseline justify-center">30<span className="text-[54px] font-bold ml-1">%</span></h1>
            <p className="text-[#666] text-[15px] mt-1 z-10 tracking-widest">聊天亲密度</p>
            
            <div className="relative mt-2">
              <Heart3D />
              <div className="absolute top-[30px] right-[0px] animate-float-y" style={{ animationDelay: '1s' }}><svg width="28" height="28" viewBox="0 0 200 200" fill="none" opacity="0.6"><path d="M100 170 C 100 170, 20 110, 20 55 C 20 25, 50 10, 75 10 C 90 10, 100 20, 100 20 C 100 20, 110 10, 125 10 C 150 10, 180 25, 180 55 C 180 110, 100 170, 100 170 Z" fill="#D9E2FF" stroke="white" strokeWidth="4" /></svg></div>
              <div className="absolute top-[60px] right-[30px] animate-float-y" style={{ animationDelay: '2s' }}><svg width="45" height="45" viewBox="0 0 200 200" fill="none" opacity="0.5"><path d="M100 170 C 100 170, 20 110, 20 55 C 20 25, 50 10, 75 10 C 90 10, 100 20, 100 20 C 100 20, 110 10, 125 10 C 150 10, 180 25, 180 55 C 180 110, 100 170, 100 170 Z" fill="#F5D0FF" stroke="white" strokeWidth="4" /></svg></div>
            </div>
            
            <button className="w-[85%] max-w-[320px] h-[58px] bg-[#5C73FF] rounded-full text-white text-[20px] font-bold mt-10 active:scale-95 transition-transform z-10 cursor-pointer tracking-wider shadow-[0_8px_20px_rgba(92,115,255,0.25)]">启用Lovekey键盘</button>
          </div>

          <div className="mx-4 bg-white rounded-3xl p-4 flex items-center justify-between mb-5 relative shadow-[0_2px_12px_rgba(0,0,0,0.02)] border border-gray-50/50">
            <button className="absolute top-3 right-3 text-gray-200 hover:text-gray-400 cursor-pointer bg-gray-100 rounded-full p-0.5"><X size={14} /></button>
            <div className="flex items-center space-x-3 w-full">
              <div className="shrink-0"><IconTicket /></div>
              <div className="flex-1">
                <p className="text-[#333] text-[13px] font-medium flex items-center">限时优惠活动，买一年送一年 <span className="text-[14px] ml-1">🎉</span></p>
                <div className="mt-1 flex items-baseline"><span className="text-[#5C73FF] font-bold text-[18px]">仅需98元</span><span className="text-[#333] font-bold text-[16px]">得24个月会员</span></div>
                <p className="text-[#FF7A92] text-[11px] font-medium bg-[#FFF0F3] px-2 py-0.5 rounded-md inline-block mt-1 tracking-wide">{formatTime(timeLeft)} 后优惠失效</p>
              </div>
              <div className="bg-[#F4EBFF] text-[#A855F7] px-3.5 py-1.5 rounded-full text-[13px] font-bold cursor-pointer hover:bg-[#EBD6FF] shrink-0 self-center mt-5 shadow-sm">一键升级</div>
            </div>
          </div>

          <div className="px-4 grid grid-cols-2 gap-4 mb-6">
            <div className="bg-white rounded-[24px] p-5 flex flex-col items-start relative h-[125px] justify-between cursor-pointer shadow-[0_2px_10px_rgba(0,0,0,0.02)] hover:shadow-md transition-shadow">
               <div className="flex justify-between w-full items-start"><IconStoreBox /><ArrowRight size={18} className="text-gray-300 transform -rotate-45" /></div>
               <div><h3 className="text-[#222] font-bold text-[17px]">聊天人设市场</h3><p className="text-[#999] text-[12px] mt-0.5">添加更多人设</p></div>
            </div>
            <div className="bg-white rounded-[24px] p-5 flex flex-col items-start relative h-[125px] justify-between cursor-pointer shadow-[0_2px_10px_rgba(0,0,0,0.02)] hover:shadow-md transition-shadow">
               <div className="flex justify-between w-full items-start"><IconBookmark /><ArrowRight size={18} className="text-gray-300 transform -rotate-45" /></div>
               <div><h3 className="text-[#222] font-bold text-[17px]">定制聊天人设</h3><p className="text-[#999] text-[12px] mt-0.5">量身打造</p></div>
            </div>
          </div>

          <div className="mx-4 mb-8 rounded-[24px] h-[95px] bg-gradient-to-r from-[#FFD1ED] via-[#FFE8F3] to-[#FFD1ED] relative overflow-hidden flex items-center px-6 shadow-sm">
             <div className="absolute right-0 top-0 bottom-0 w-[50%] opacity-40 mix-blend-multiply bg-[url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI4IiBoZWlnaHQ9IjgiPjxyZWN0IHdpZHRoPSI4IiBoZWlnaHQ9IjgiIGZpbGw9IiNmZmYiPjwvcmVjdD48cGF0aCBkPSJNMCAwTDggOFpNOCAwTDAgOFoiIHN0cm9rZT0iI2YwZiIgY3Ryb2tlLXdpZHRoPSIxIiBvcGFjaXR5PSIwLjEiPjwvcGF0aD48L3N2Zz4=')]"></div>
             <div className="z-10 w-[60%]"><h3 className="text-[#1A1A1A] font-black italic text-[22px] text-stroke-black transform -rotate-2 leading-tight tracking-wide drop-shadow-sm">键盘轻松应对<br/>各种场景</h3></div>
             <div className="absolute right-[-5px] top-3 flex z-10">
                <div className="w-[60px] h-[70px] bg-[#FFB6C1] rounded-[16px] transform -rotate-[10deg] border-[3px] border-white shadow-md flex items-center justify-center relative right-[-20px] z-10"><span className="text-[34px]">👩</span><div className="absolute -bottom-2 right-[-5px] bg-[#E8A5FF] text-white text-[9px] px-1.5 rounded-sm whitespace-nowrap transform rotate-[10deg]">脱单局</div></div>
                <div className="w-[60px] h-[70px] bg-[#B0C4DE] rounded-[16px] transform rotate-[10deg] border-[3px] border-white shadow-md flex items-center justify-center mt-3"><span className="text-[34px]">👦</span><div className="absolute -top-2 left-[-10px] bg-[#B5BFFF] text-white text-[9px] px-1.5 rounded-sm whitespace-nowrap transform -rotate-[10deg]">社交局</div></div>
             </div>
          </div>

          <div className="px-4 mb-8">
            <div className="flex justify-between items-center mb-4 px-1">
               <h2 className="text-[19px] font-bold text-[#222]">我的键盘</h2>
               <span className="text-[#999] text-[13px] flex items-center cursor-pointer hover:text-gray-600 tracking-wide">修改键盘排序 <ChevronLeft size={14} className="transform rotate-180 ml-0.5" /></span>
            </div>
            <div className="grid grid-cols-3 gap-3">
              {['🍬 高情商', '😘 心动狙击', '😆 幽默', '☀️ 暖男', '💋 暧昧拉扯', '🎀 情场高手', '👨‍⚖️ 温柔大叔', '🎁 风流浪子', '😋 幽默有梗'].map((text, i) => (
                 <div key={i} className="bg-white h-[54px] rounded-[16px] flex items-center justify-center text-[14px] font-medium text-[#333] cursor-pointer shadow-[0_2px_8px_rgba(0,0,0,0.02)] hover:shadow-md hover:bg-gray-50/50 transition-all border border-gray-50">{text}</div>
              ))}
            </div>
          </div>

          <div className="px-4 mb-4">
            <h2 className="text-[19px] font-bold text-[#222] mb-4 px-1">更多</h2>
            <div className="grid grid-cols-2 gap-4">
               <div className="bg-[#F7F8FA] rounded-[24px] p-5 relative h-[120px] flex flex-col justify-between cursor-pointer hover:bg-[#F0F2F5] transition-colors"><div className="flex justify-between items-start"><div className="text-[#333]"><IconTShirt /></div><ArrowRight size={16} className="text-gray-300 transform -rotate-45" /></div><div><h3 className="text-[#222] font-bold text-[16px]">键盘皮肤</h3><p className="text-[#999] text-[12px] mt-1 tracking-wide">更多皮肤随心挑</p></div></div>
               <div className="bg-[#F7F8FA] rounded-[24px] p-5 relative h-[120px] flex flex-col justify-between cursor-pointer hover:bg-[#F0F2F5] transition-colors" onClick={onShowKeyboard}><div className="flex justify-between items-start"><div className="text-[#333]"><IconKeyboardGrid /></div><ArrowRight size={16} className="text-gray-300 transform -rotate-45" /></div><div><h3 className="text-[#222] font-bold text-[16px]">切换输入法</h3><p className="text-[#999] text-[12px] mt-1 tracking-wide">选择👉Lovekey键盘</p></div></div>
               <div className="bg-[#F7F8FA] rounded-[24px] p-5 relative h-[120px] flex flex-col justify-between cursor-pointer col-span-1 w-full hover:bg-[#F0F2F5] transition-colors"><div className="flex justify-between items-start"><div className="text-[#333]"><IconFAQ /></div><ArrowRight size={16} className="text-gray-300 transform -rotate-45" /></div><div><h3 className="text-[#222] font-bold text-[16px]">常见问题</h3><p className="text-[#999] text-[12px] mt-1 tracking-wide">键盘使用帮助</p></div></div>
            </div>
          </div>
        </div>
      </div>

      <div className="absolute right-[-15px] top-[400px] z-30 pointer-events-none drop-shadow-2xl">
        <div className="relative animate-float-y-slow">
          <div className="absolute -top-3 left-2 text-[#88AAFF] text-[16px]">✦</div><div className="absolute top-6 -left-3 text-[#88AAFF] text-[12px]">✦</div>
          <div className="w-[105px] h-[105px] bg-gradient-to-br from-[#E8EEFF] to-[#A0B8FF] rounded-[28px] border-[3px] border-white flex flex-col items-center justify-center shadow-[0_10px_30px_rgba(92,115,255,0.4)] pointer-events-auto cursor-pointer active:scale-95 transition-transform overflow-hidden relative">
            <div className="absolute top-1 right-2 bg-[#5C73FF] text-white text-[9px] font-bold px-1.5 py-0.5 rounded-full transform -rotate-6 z-10 whitespace-nowrap">新户专享</div>
            <div className="mt-4 flex flex-col items-center text-center">
              <span className="text-[18px] font-black italic text-transparent bg-clip-text bg-gradient-to-r from-[#FFE03B] to-[#FFB300] leading-none text-stroke-blue drop-shadow-md" style={{ WebkitTextStroke: '0.8px #4B66FF' }}>买一月</span>
              <span className="text-[18px] font-black italic text-transparent bg-clip-text bg-gradient-to-r from-[#FFE03B] to-[#FFB300] leading-tight text-stroke-blue drop-shadow-md mt-0.5" style={{ WebkitTextStroke: '0.8px #4B66FF' }}>送一月</span>
            </div>
            <div className="bg-[#FF4B6B] text-white text-[12px] font-bold px-3 py-[2px] rounded-full mt-1.5 shadow-md z-10 tracking-widest">59:20</div>
            <div className="absolute bottom-0 left-0 w-full h-3 flex gap-1.5 px-3 opacity-80"><div className="w-1.5 h-3 bg-[#FFE03B] transform -skew-x-12"></div><div className="w-1.5 h-3 bg-[#00E676] transform -skew-x-12"></div><div className="w-1.5 h-3 bg-[#FF4081] transform -skew-x-12"></div></div>
          </div>
        </div>
      </div>
    </div>
  );
};

// --- 人设市场数据与组件 ---
const CATEGORIES_REPLY = [
  { id: 'rank', label: '本周排行', special: 'gold' },
  { id: 'new', label: '新上架', special: 'pink' },
  { id: 'spring', label: '春节嘴替', special: 'red' },
  { id: 'valentine', label: '心动情人节' },
  { id: 'daily', label: '日常必备' },
  { id: 'emotion', label: '感情升温' },
  { id: 'night', label: '深夜热聊' },
  { id: 'toxic', label: '“嘴毒”王者' },
  { id: 'persona', label: '个人设' },
  { id: 'campus', label: '校园恋习生' },
  { id: 'roleplay', label: '趣味扮演' },
  { id: 'dialect', label: '特色方言' },
  { id: 'zodiac', label: '十二星座' },
  { id: 'mbti', label: 'MBTI' },
  { id: 'work', label: '玩转职场' },
  { id: 'love', label: '恋爱零距离' },
];

const CATEGORIES_TALK = [
  { id: 'all', label: '全部', special: 'orange' },
  { id: 'new', label: '新上架', special: 'pink' },
  { id: 'spring', label: '春节嘴替', special: 'normal' },
  { id: 'chat', label: '聊天必备', special: 'normal' },
  { id: 'emotion', label: '感情升温', special: 'normal' },
  { id: 'toxic', label: '嘴毒王者', special: 'normal' },
  { id: 'funny', label: '搞怪逗趣', special: 'normal' },
  { id: 'moments', label: '朋友圈', special: 'normal' },
  { id: 'work', label: '玩转职场', special: 'normal' },
];

const PERSONAS_REPLY = [
  { id: 'r1', rank: 1, title: '高情商', desc: '把难说的话说得漂亮又舒服...', usage: '8395.7w', added: true },
  { id: 'r2', rank: 2, title: '幽默', desc: '把尴尬变笑点，聊天永远不...', usage: '4894.2w', added: true },
  { id: 'r3', rank: 3, title: '情场高手', desc: '节奏把握精准，情绪进退自...', usage: '2100.1w', added: true },
  { id: 'r4', rank: 4, title: '心动狙击', desc: '不露痕迹地撩，句句都在加...', usage: '7385.4w', added: true },
  { id: 'r5', rank: 5, title: '暧昧拉扯', desc: '话留余地，情绪却恰到好处...', usage: '2447.3w', added: true },
  { id: 'r6', rank: 6, title: '暖男', desc: '细节关怀在线，说话让人安...', usage: '1796.3w', added: true },
  { id: 'r7', rank: 7, title: '幽默有梗', desc: '逗她开心，是最重要的小事', usage: '989.7w', added: true },
  { id: 'r8', rank: 8, title: '情绪价值', desc: '懂共情会安慰，稳稳接住你...', usage: '502w', added: false },
  { id: 'r9', rank: 9, title: '双商在线', desc: '通透清醒进退有度，相处舒...', usage: '477w', added: true },
  { id: 'r10', rank: 10, title: '温柔体贴', desc: '话语细腻周到，让人感到被...', usage: '776w', added: true },
];

const PERSONAS_TALK = [
  { id: 't1', title: '情绪价值', desc: '懂情绪又懂安慰，是懂人心的高手💕', added: false },
  { id: 't2', title: '心动狙击', desc: '懂分寸会撩，气氛刚刚好😉', added: false },
  { id: 't3', title: '真情告白', desc: '不拐弯，直接把心掏给你❤️', added: false },
  { id: 't4', title: '自然表达', desc: '说话变得自然流畅👏', added: true },
  { id: 't5', title: '高情商', desc: '说话有温度，让人舒服到想续聊💬', added: true },
  { id: 't6', title: '幽默', desc: '自带笑点buff，让聊天不冷场🤣', added: true },
  { id: 't7', title: '双商在线', desc: '逻辑清晰又懂人情，稳得一批🧠', added: true },
  { id: 't8', title: '夸夸TA', desc: '彩虹屁满分，让人开心到飞起🌈', added: true },
  { id: 't9', title: '咸鱼躺平', desc: '不卷不装，佛系聊天最高级🛋️', added: false },
];

const MARKET_AVATARS = {
  'r1': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r1&backgroundColor=f5f5f5' },
  'r2': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r2&backgroundColor=e6f0fa' },
  'r3': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r3&backgroundColor=fae6e6' },
  'r4': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r4&backgroundColor=f0e6fa' },
  'r5': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r5&backgroundColor=e6fae6' },
  'r6': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r6&backgroundColor=e6faf0' },
  'r7': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r7&backgroundColor=fafae6' },
  'r8': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r8&backgroundColor=fae6f0' },
  'r9': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r9&backgroundColor=e6f0fa' },
  'r10': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=r10&backgroundColor=f5f5f5' },
  't1': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t1&backgroundColor=ffebee' },
  't2': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t2&backgroundColor=fff3e0' },
  't3': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t3&backgroundColor=fce4ec' },
  't4': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t4&backgroundColor=e3f2fd' },
  't5': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t5&backgroundColor=e8f5e9' },
  't6': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t6&backgroundColor=e0f7fa' },
  't7': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t7&backgroundColor=f3e5f5' },
  't8': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t8&backgroundColor=fff8e1' },
  't9': { img: 'https://api.dicebear.com/7.x/micah/svg?seed=t9&backgroundColor=eceff1' },
};

const MarketCategoryTag = ({ cat, onClick, active }) => {
  if (cat.special === 'gold') return <div onClick={onClick} className={`flex items-center px-3.5 py-1.5 rounded-full bg-gradient-to-r from-[#FFF4D6] to-[#FFE0A3] text-[#A66E00] text-[13px] font-bold cursor-pointer shrink-0 border-[1.5px] border-[#FFD980] transition-all ${active ? 'shadow-md scale-[1.03]' : 'shadow-sm'}`}>🌿 {cat.label} 🌿</div>;
  if (cat.special === 'pink') return <div onClick={onClick} className={`flex items-center px-3.5 py-1.5 rounded-full bg-[#FFF0F5] text-[#FF4B8B] text-[13px] font-bold cursor-pointer shrink-0 border-[1.5px] border-[#FFD1E3] transition-all ${active ? 'shadow-md scale-[1.03]' : 'shadow-sm'}`}><span className="italic font-black mr-1 text-[#FF85E3]">New</span> {cat.label}</div>;
  if (cat.special === 'red') return <div onClick={onClick} className={`flex items-center px-3.5 py-1.5 rounded-full bg-[#FFF0F0] text-[#E02020] text-[13px] font-bold border-[1.5px] border-[#FFD6D6] cursor-pointer shrink-0 transition-all ${active ? 'shadow-md scale-[1.03]' : 'shadow-sm'}`}>🧧 {cat.label} 🧧</div>;
  if (cat.special === 'orange') return <div onClick={onClick} className={`flex items-center px-4 py-1.5 rounded-full bg-[#FFA033] text-white text-[13px] font-bold cursor-pointer shrink-0 transition-all ${active ? 'shadow-md scale-[1.03]' : 'shadow-sm'}`}>{cat.label}</div>;
  return <div onClick={onClick} className={`flex items-center px-3.5 py-1.5 rounded-full bg-white border-[1.5px] text-[13px] font-bold cursor-pointer shrink-0 hover:bg-[#F9FAFB] transition-all ${active ? 'border-[#5C73FF] text-[#5C73FF] shadow-md scale-[1.03]' : 'border-[#F3F4F6] text-[#555] shadow-sm'}`}>{cat.label}</div>;
};

const RankBadge = ({ rank }) => {
  const isTop1 = rank === 1;
  const isTop2 = rank === 2;
  const isTop3 = rank === 3;
  
  if (rank <= 3) {
    let colors = {
       bg: isTop1 ? 'from-[#FFEDA6] to-[#FFC94D]' : isTop2 ? 'from-[#E1ECFF] to-[#A3C4FF]' : 'from-[#FFDEC2] to-[#FFA673]',
       text: isTop1 ? 'text-[#8A5A00]' : isTop2 ? 'text-[#365A96]' : 'text-[#8A4613]'
    };
    return (
      <div className="flex flex-col items-center justify-center relative w-[28px] h-[36px] drop-shadow-[0_2px_4px_rgba(0,0,0,0.15)]">
        <div className={`absolute inset-0 bg-gradient-to-b ${colors.bg}`} style={{ clipPath: 'polygon(0 0, 100% 0, 100% 75%, 50% 100%, 0 75%)', borderRadius: '4px 4px 0 0' }}></div>
        <div className="relative z-10 flex flex-col items-center pt-[3px]">
           <span className={`text-[8px] font-black leading-none ${colors.text} tracking-tighter scale-90`}>TOP</span>
           <span className={`text-[15px] font-black leading-none mt-[1px] ${colors.text}`}>{rank}</span>
        </div>
      </div>
    );
  }
  return <div className="text-[20px] font-black italic text-[#0F172A] w-[28px] text-center tracking-tighter pr-1">{rank}</div>;
};

const PersonaMarketScreen = ({ onShowKeyboard }) => {
  const [subTab, setSubTab] = useState('reply'); // 'reply' | 'talk'
  const [category, setCategory] = useState('rank');
  const [showCategorySheet, setShowCategorySheet] = useState(false);
  const [addedIds, setAddedIds] = useState(
    [...PERSONAS_REPLY, ...PERSONAS_TALK].filter(p => p.added).map(p => p.id)
  );

  const toggleAdd = (id) => {
    if (addedIds.includes(id)) {
       setAddedIds(addedIds.filter(i => i !== id));
    } else {
       setAddedIds([...addedIds, id]);
    }
  };

  const activeCategories = subTab === 'reply' ? CATEGORIES_REPLY : CATEGORIES_TALK;
  const activePersonas = subTab === 'reply' ? PERSONAS_REPLY : PERSONAS_TALK;

  return (
    <div className="absolute inset-0 bg-[#F4F5FB] flex flex-col">
      {/* 顶部背景光晕 */}
      <div className="absolute top-0 w-full h-[280px] bg-gradient-to-b from-[#E5EBFF] via-[#F4F5FB] to-[#F4F5FB] pointer-events-none z-0"></div>
      <div className="absolute top-[-50px] right-[-50px] w-[200px] h-[200px] bg-[#D4DFFF] rounded-full blur-[60px] opacity-60 pointer-events-none z-0"></div>

      {/* Header */}
      <div className="pt-12 pb-2 px-5 flex justify-between items-center relative z-20">
        <h1 className="text-[22px] font-black text-[#1A1A1A] tracking-wider">人设市场</h1>
        <button onClick={onShowKeyboard} className="bg-white/90 backdrop-blur-md px-3.5 py-1.5 rounded-full flex items-center shadow-[0_2px_8px_rgba(0,0,0,0.04)] border border-white">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#666" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="mr-1.5"><rect x="2" y="6" width="20" height="12" rx="3"></rect><circle cx="7" cy="10" r="1" fill="#666" stroke="none"></circle><circle cx="12" cy="10" r="1" fill="#666" stroke="none"></circle><circle cx="17" cy="10" r="1" fill="#666" stroke="none"></circle><rect x="7" y="13" width="10" height="2" rx="1" fill="#666" stroke="none"></rect></svg>
          <span className="text-[12px] font-bold text-[#555]">我的键盘</span>
        </button>
      </div>

      <div className="flex-1 overflow-y-auto no-scrollbar pb-[120px] relative z-10" style={{ WebkitOverflowScrolling: 'touch' }}>
        
        {/* Banner 区域 */}
        <div className="mx-4 mt-2 rounded-[20px] h-[90px] bg-gradient-to-r from-[#FFD1ED] via-[#FFE8F3] to-[#FFD1ED] relative overflow-hidden flex items-center px-5 shadow-sm">
           <div className="absolute right-0 top-0 bottom-0 w-[50%] opacity-40 mix-blend-multiply bg-[url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI4IiBoZWlnaHQ9IjgiPjxyZWN0IHdpZHRoPSI4IiBoZWlnaHQ9IjgiIGZpbGw9IiNmZmYiPjwvcmVjdD48cGF0aCBkPSJNMCAwTDggOFpNOCAwTDAgOFoiIHN0cm9rZT0iI2YwZiIgY3Ryb2tlLXdpZHRoPSIxIiBvcGFjaXR5PSIwLjEiPjwvcGF0aD48L3N2Zz4=')]"></div>
           <div className="absolute top-0 left-0 bg-[#C88B4B] text-white text-[10px] font-bold px-2 py-0.5 rounded-br-[10px] z-20 shadow-sm">热门推荐</div>
           <div className="z-10 w-[60%] mt-2"><h3 className="text-[#1A1A1A] font-black italic text-[20px] text-stroke-black transform -rotate-2 leading-tight tracking-wide drop-shadow-sm">键盘轻松应对<br/>各种场景</h3></div>
           <div className="absolute right-[-10px] top-3 flex z-10">
              <div className="w-[50px] h-[60px] bg-[#FFB6C1] rounded-[14px] transform -rotate-[10deg] border-[2px] border-white shadow-md flex items-center justify-center relative right-[-15px] z-10"><span className="text-[28px]">👩</span><div className="absolute -bottom-2 right-[-5px] bg-[#E8A5FF] text-white text-[8px] px-1.5 rounded-sm whitespace-nowrap transform rotate-[10deg]">脱单局</div></div>
              <div className="w-[50px] h-[60px] bg-[#B0C4DE] rounded-[14px] transform rotate-[10deg] border-[2px] border-white shadow-md flex items-center justify-center mt-3"><span className="text-[28px]">👦</span><div className="absolute -top-2 left-[-10px] bg-[#B5BFFF] text-white text-[8px] px-1.5 rounded-sm whitespace-nowrap transform -rotate-[10deg]">社交局</div></div>
           </div>
        </div>

        {/* Tabs 切换 */}
        <div className="flex px-4 mt-6 h-[50px]">
          {/* 帮你回 Tab */}
          <div className={`flex-1 flex flex-col items-center justify-center relative cursor-pointer z-10 transition-all ${subTab === 'reply' ? 'bg-gradient-to-b from-[#EAEFFF] to-[#F4F5FB] rounded-t-[18px] border-t-[1.5px] border-l-[1.5px] border-r-[1.5px] border-white/80 shadow-[0_-4px_10px_rgba(0,0,0,0.02)]' : 'bg-transparent'}`} onClick={() => setSubTab('reply')}>
             <span className={`text-[19px] font-black transition-colors ${subTab === 'reply' ? 'text-[#1A1A1A]' : 'text-[#A0A5B5]'}`}>帮你回</span>
             {subTab === 'reply' && <div className="absolute bottom-1.5 w-6 h-[3.5px] bg-[#FFD233] rounded-full"></div>}
          </div>
          
          {/* 超会说 Tab */}
          <div className={`flex-1 flex flex-col items-center justify-center relative cursor-pointer z-10 transition-all ${subTab === 'talk' ? 'bg-gradient-to-b from-[#EAEFFF] to-[#F4F5FB] rounded-t-[18px] border-t-[1.5px] border-l-[1.5px] border-r-[1.5px] border-white/80 shadow-[0_-4px_10px_rgba(0,0,0,0.02)]' : 'bg-transparent'}`} onClick={() => setSubTab('talk')}>
             <span className={`text-[19px] font-black transition-colors flex items-start ${subTab === 'talk' ? 'text-[#1A1A1A]' : 'text-[#A0A5B5]'}`}>
                超会说 <SparkleIcon className="w-3 h-3 ml-0.5 mt-0.5 opacity-80" />
             </span>
             {subTab === 'talk' && <div className="absolute bottom-1.5 w-6 h-[3.5px] bg-[#FFD233] rounded-full"></div>}
          </div>
        </div>

        {/* 分类标签 */}
        <div className="flex items-center mt-3 px-4 relative z-20">
           <div className="flex-1 overflow-x-auto no-scrollbar flex gap-2 items-center pr-8 pb-2 pt-1">
              {activeCategories.map(cat => <MarketCategoryTag key={cat.id} cat={cat} active={category === cat.id} onClick={() => setCategory(cat.id)} />)}
           </div>
           <div className="w-[30px] h-full absolute right-8 top-0 bg-gradient-to-l from-[#F4F5FB] via-[#F4F5FB] to-transparent pointer-events-none"></div>
           <button onClick={() => setShowCategorySheet(true)} className="absolute right-4 bg-[#F4F5FB] w-8 h-8 flex items-center justify-center cursor-pointer z-10 text-[#A0A5B5] pl-1">
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round"><line x1="4" y1="12" x2="20" y2="12"></line><line x1="4" y1="6" x2="20" y2="6"></line><line x1="4" y1="18" x2="20" y2="18"></line></svg>
           </button>
        </div>

        {/* 列表内容区域 */}
        <div className="mt-4 px-4 pb-4">
          {subTab === 'reply' ? (
            <div className="flex flex-col gap-3">
              {activePersonas.map((p, idx) => {
                const isAdded = addedIds.includes(p.id);
                const isYellowTag = p.id === 'r2'; // Top 2 特殊浅黄标签
                return (
                  <div key={p.id} className="bg-white rounded-[24px] p-4 flex items-center shadow-[0_2px_12px_rgba(0,0,0,0.03)] relative cursor-pointer active:scale-[0.98] transition-transform" onClick={() => toggleAdd(p.id)}>
                     
                     {/* Left: 错层设计的徽章 + 头像组合 */}
                     <div className="relative flex items-center shrink-0 w-[84px] h-[64px]">
                        {/* Avatar */}
                        <div className="absolute right-0 w-[64px] h-[64px] rounded-full flex items-center justify-center border-[2px] border-white shadow-[0_4px_12px_rgba(0,0,0,0.08)] overflow-hidden bg-[#F4F5FB]">
                           <img src={MARKET_AVATARS[p.id].img} alt="" className="w-[120%] h-[120%] object-cover mt-2" />
                           <div className="absolute inset-0 rounded-full shadow-[inset_0_4px_8px_rgba(255,255,255,0.6),inset_0_-4px_8px_rgba(0,0,0,0.1)] pointer-events-none"></div>
                        </div>
                        {/* Rank Badge 覆盖层 */}
                        <div className="relative z-10 ml-[-2px]">
                           <RankBadge rank={p.rank} />
                        </div>
                     </div>
                     
                     {/* Middle: 文本信息 */}
                     <div className="flex-1 ml-4 min-w-0 pr-2 flex flex-col justify-center py-1">
                        <h3 className="text-[17px] font-bold text-[#1A1A1A] truncate mb-1">{p.title}</h3>
                        <p className="text-[#999] text-[13px] truncate mb-2">{p.desc}</p>
                        <div className="flex items-center">
                          <div className={`text-[11px] px-2.5 py-[3px] rounded-md font-medium inline-flex items-center leading-none ${isYellowTag ? 'bg-[#FFF6E5] text-[#B28200]' : 'bg-[#F4F6F9] text-[#999]'}`}>
                            {p.usage}人在用
                          </div>
                        </div>
                     </div>
                     
                     {/* Right: 按钮 */}
                     <div className="shrink-0 pl-2">
                        <button onClick={(e) => { e.stopPropagation(); toggleAdd(p.id); }} className={`w-8 h-8 rounded-full flex items-center justify-center transition-colors ${isAdded ? 'bg-[#F4F5FB] text-[#333]' : 'bg-[#5C73FF] text-white shadow-[0_4px_10px_rgba(92,115,255,0.3)]'}`}>
                           {isAdded ? <Check size={16} strokeWidth={3} /> : <span className="text-[20px] font-bold leading-none mb-0.5">+</span>}
                        </button>
                     </div>
                  </div>
                );
              })}
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-3">
              {activePersonas.map((p) => {
                const isAdded = addedIds.includes(p.id);
                return (
                  <div key={p.id} className="bg-white rounded-[24px] p-4 flex flex-col relative shadow-[0_2px_12px_rgba(0,0,0,0.03)] h-[175px] cursor-pointer active:scale-[0.98] transition-transform overflow-hidden" onClick={() => toggleAdd(p.id)}>
                     <div className="relative z-10 flex flex-col items-start mt-1">
                        <h3 className="text-[17px] font-bold text-[#1A1A1A] relative inline-block">
                          {p.title}
                          {/* 标志性的波浪下划线 */}
                          <svg className="absolute -bottom-1.5 left-0 w-[110%] text-[#FFD233]" height="6" viewBox="0 0 100 12" preserveAspectRatio="none">
                            <path d="M0,6 Q12,0 25,6 T50,6 T75,6" stroke="currentColor" strokeWidth="3" fill="none" strokeLinecap="round" />
                          </svg>
                        </h3>
                        <p className="text-[#888] text-[13px] mt-3.5 leading-[1.4] max-w-[70%]">{p.desc}</p>
                     </div>
                     
                     {/* 巨大的背景引号 */}
                     <div className="absolute left-3 bottom-0 text-[#F4F5FB] text-[80px] font-serif font-black leading-none pointer-events-none z-0 tracking-tighter">“</div>
                     
                     {/* 头像 */}
                     <div className="absolute right-2 top-10 w-[52px] h-[52px] rounded-[16px] flex items-center justify-center z-0 bg-[#F4F5FB] overflow-hidden shadow-sm">
                        <img src={MARKET_AVATARS[p.id].img} alt="" className="w-[120%] h-[120%] object-cover mt-1" />
                     </div>
                     
                     {/* 按钮 */}
                     <button className={`absolute right-3 bottom-3 w-[28px] h-[28px] rounded-full flex items-center justify-center transition-colors z-20 ${isAdded ? 'bg-[#F4F5FB] text-[#333]' : 'bg-[#FFA033] text-white shadow-[0_4px_10px_rgba(255,160,51,0.3)]'}`}>
                        {isAdded ? <Check size={14} strokeWidth={3} /> : <span className="text-[18px] font-bold leading-none mb-0.5">+</span>}
                     </button>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      </div>

      {/* 底部全部分类弹窗 */}
      {showCategorySheet && (
        <div className="absolute inset-0 z-[200] bg-black/50 backdrop-blur-sm flex flex-col justify-end animate-fade-in" onClick={() => setShowCategorySheet(false)}>
           <div className="w-full bg-[#F4F5FB] rounded-t-[32px] p-6 pb-12 shadow-[0_-10px_40px_rgba(0,0,0,0.5)] animate-slide-up relative" onClick={e => e.stopPropagation()}>
              <div className="w-10 h-1.5 bg-[#D1D5DB] rounded-full mx-auto mb-6"></div>
              <h3 className="text-center text-[#1A1A1A] font-bold text-[18px] mb-6 flex items-center justify-center">点击选择人设标签 <span className="text-[22px] ml-1">👇</span></h3>
              <div className="flex flex-wrap gap-3 justify-center">
                 {activeCategories.map(cat => (
                   <MarketCategoryTag key={cat.id} cat={cat} active={category === cat.id} onClick={() => { setCategory(cat.id); setShowCategorySheet(false); }} />
                 ))}
              </div>
           </div>
        </div>
      )}
    </div>
  );
};

const MainAppShell = () => {
  const [activeTab, setActiveTab] = useState('home');
  const [showLogin, setShowLogin] = useState(false);
  const [showKeyboard, setShowKeyboard] = useState(false);

  return (
    <div className="w-full h-full relative flex flex-col bg-[#F4F5FB]">
      <div className="flex-1 relative min-h-0 overflow-hidden">
         {activeTab === 'home' && <AppHomeScreen onShowLogin={() => setShowLogin(true)} onShowKeyboard={() => setShowKeyboard(true)} />}
         {activeTab === 'market' && <PersonaMarketScreen onShowKeyboard={() => setShowKeyboard(true)} />}
         {activeTab === 'keyboard' && <div className="p-8 text-center text-gray-500 mt-20">我的键盘模块开发中...</div>}
      </div>
      
      <div className="h-[75px] bg-[#F4F5FB] w-full absolute bottom-0 z-[100] flex shadow-[0_-4px_20px_rgba(0,0,0,0.02)] pb-5 pt-2">
        <NavTabButton active={activeTab === 'home'} onClick={() => setActiveTab('home')} icon={AppNavIconChat} label="首页" />
        <NavTabButton active={activeTab === 'market'} onClick={() => setActiveTab('market')} icon={AppNavIconMarket} label="人设市场" />
        <NavTabButton active={activeTab === 'keyboard'} onClick={() => setActiveTab('keyboard')} icon={AppNavIconKeyboard} label="我的键盘" />
      </div>

      {showLogin && <LoginModal onClose={() => setShowLogin(false)} />}
      {showKeyboard && <KeyboardSwitchModal onClose={() => setShowKeyboard(false)} />}
    </div>
  );
};


// ==========================================
// 主应用入口 (带有全局 Step 控制)
// ==========================================
export default function App() {
  const [step, setStep] = useState(0);
  const [selectedIds, setSelectedIds] = useState([]);
  const [showDownsell, setShowDownsell] = useState(false);

  const togglePersona = (id) => setSelectedIds(prev => prev.includes(id) ? prev.filter(i => i !== id) : [...prev, id]);
  const handleNext = () => setStep(prev => prev + 1);
  const handlePrev = () => setStep(prev => prev - 1);
  const handlePaywallClose = () => setShowDownsell(true);
  const handleEnterApp = () => { setShowDownsell(false); setStep(10); };

  // 状态栏颜色
  const isDarkStatus = [0, 5, 8, 10].includes(step);

  return (
    <div className="min-h-screen bg-[#E5E7EB] flex items-center justify-center p-4">
      <AnimationStyles />
      <div className="w-full max-w-[393px] h-[852px] bg-white rounded-[50px] shadow-2xl overflow-hidden relative border-[12px] border-[#121212] flex flex-col">
        
        {/* 统一顶部状态栏 */}
        <div className={`h-12 px-8 flex justify-between items-center z-[150] absolute top-0 w-full ${isDarkStatus ? 'text-[#333]' : 'text-white'}`}>
           <span className="text-[15px] font-bold tracking-tight">09:50</span>
           <div className="flex space-x-1.5 items-center">
             <div className={`w-4 h-4 rounded-full scale-[0.6] ${isDarkStatus ? 'bg-[#333]' : 'bg-white'}`}></div>
             <div className={`w-5 h-2.5 border-[1.5px] rounded-[3px] p-[1px] ${isDarkStatus ? 'border-[#333]' : 'border-white'}`}>
               <div className={`w-[70%] h-full ${isDarkStatus ? 'bg-[#333]' : 'bg-white'}`}></div>
             </div>
           </div>
        </div>

        {/* 页面路由 */}
        <div className="flex-1 relative min-h-0 overflow-hidden pt-10">
           {step === 0 && <WelcomeScreen onNext={handleNext} />}
           {step === 1 && <GenderScreen onNext={handleNext} />}
           {step === 2 && <BirthdayScreen onNext={handleNext} onPrev={handlePrev} />}
           {step === 3 && <PersonaScreen onPrev={handlePrev} onNext={handleNext} selectedIds={selectedIds} onToggle={togglePersona} />}
           {step === 4 && <LoadingScreen onNext={handleNext} />}
           {step === 5 && <SetupKeyboardScreen onNext={handleNext} />}
           {step === 6 && <KeyboardSelectScreen onNext={handleNext} />}
           {step === 7 && <ChatKeyboardScreen isTutorial={true} onComplete={handleNext} />}
           {step === 8 && <PaywallMainScreen onNext={handleEnterApp} onDownsell={handlePaywallClose} />}
           {step === 9 && <ChatKeyboardScreen isTutorial={false} onComplete={() => {}} />}
           {step === 10 && <MainAppShell />}
        </div>
        
        {/* 付费挽留弹窗 */}
        {showDownsell && <DownsellModal onClose={handleEnterApp} onNext={handleEnterApp} />}
        
        {/* 底部系统导航条 (安全区指示器) */}
        <div className={`h-6 flex items-center justify-center absolute bottom-1 w-full z-[250] pointer-events-none`}>
           <div className={`w-32 h-[5px] rounded-full ${step === 10 || step === 8 ? 'bg-[#333]' : 'bg-black/20'}`}></div>
        </div>
      </div>
    </div>
  );
}
