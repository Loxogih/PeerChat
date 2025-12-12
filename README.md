# 🚀 P2P Chat System

A fully distributed peer-to-peer chat application written in Java, enabling direct communication without a central server.

## ✨ Features

* **Direct Peer-to-Peer Messaging:** Connect and chat without any central server.
* **Message Types:** Broadcast to all peers, private messages, or group messages.
* **Group Management:** Create, add, or remove members from chat groups.
* **Persistent Chat History:** Each user stores their own message logs.
* **Terminal Interface:** Simple and intuitive CLI for easy interaction.

## 🏃 How to Run

### 1️⃣ Compile the code

```bash
cd src
javac *.java
```

### 2️⃣ Run multiple users (in separate terminals)

**Terminal 1 - Alice:**

```bash
java Main
# Username: Alice, Port: 5000
```

**Terminal 2 - Bob:**

```bash
java Main
# Username: Bob, Port: 5001
# Connect → 4 → localhost → 5000
```

**Terminal 3 - Charlie:**

```bash
java Main
# Username: Charlie, Port: 5002
# Connect → 4 → localhost → 5000
```


## 👥 Team

Samy Charallah ,Ramzi Cherfaoui

## ⚠ Notes

* Run all users on the same machine using different ports (e.g., 5000, 5001, 5002).
* Connect everyone to one peer (e.g., Alice) to form a full mesh network.
* Only group creators can manage group members.
* Terminal-based interface ensures simplicity and low resource usage.


