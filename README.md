---

# Procedural Fish Animation

This project is a simple 2D procedural animation of a fish using inverse kinematics (IK). The fish follows the mouse cursor smoothly, with its body and fins dynamically adjusting to the movement.

![Fish Animation](https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExcDV6bmRwbmRldTl4b2ZkamEybWNwY3Z6cHk1eTZjeWkzYjJubms3ZSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/20BFm5Yh7LxC4zO6tJ/giphy.gif)

## Features
- **Procedural Animation**: The fish's body and fins move dynamically based on the mouse position.
- **Smooth Movement**: The fish follows the mouse cursor with a slight offset to avoid snapping.
- **Customizable**: Easily adjust the fish's size, speed, and behavior by modifying the code.
- **Interactive Controls**: Control the fish's animation using keybinds for more flexibility.

## How It Works
The fish is composed of a chain of joints, and its movement is controlled using inverse kinematics. The head of the fish follows the mouse cursor, while the rest of the body smoothly adjusts to maintain a natural flow.

## Keybindings
The following keybindings allow you to modify the behavior of the fish during the animation:

- **C**: Toggle the visibility of the joint chain (show/hide the skeleton of the fish).
- **A**: Toggle autonomous movement. When enabled, the fish will move on its own, without following the mouse.
- **F**: Toggle filled circles on/off. This controls whether the fish's fins and body joints are drawn as filled circles.
