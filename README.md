# RafBook
### Distributed system for managing text files and images

---

## 1. Introduction

This distributed system enables working with ASCII-encoded text files and images. The system provides the following functionalities to users:

#### Functionalities

- **Adding a New File**: Allows the user to add a new file to the system with a unique name and path.
- **Retrieving Files**: Users can retrieve any file from the distributed system.
- **Adding Friend Nodes**: The system supports adding new friend nodes to increase efficiency.
- **Deleting a File**: Deletes a file from the local node (server).
- **Optimized System Topology**: The organization of the system enables faster searching and retrieval of files.
- **Fault Tolerance**: The system is resistant to failures and provides data recovery options.

This system is designed to ensure reliability and speed in working with distributed files and images.


## 2. Identification
### 2.1 Node Identification
Each node is identified by:
- Universally Unique Identifier (UUID)
- IPv4 Address
- Port Number
- Chord Identifier (hash of the address and port)

### 2.2 Message Identification
Each message is identified by:
- UUID
- Message Type
- Message Text
- Sender's Address and Port
- Receiver's Address and Port

### 2.3 File Identification
Each file is identified by:
- UUID
- File Name
- File Type
- File Content
- Chord Identifier

### 2.4 Backup Identification
Each backup is identified by:
- Owner’s Chord ID
- Backup Location
- File Name
- File Type

## 3. Communication Protocol
### 3.1 System Configuration
Nodes are configured using a local configuration file containing:
- Chord Size (number of chord IDs assignable)
- Root Directory
- Bootstrap Port (predefined for connecting nodes)
- Node Port (for socket communication)
- Low Waiting Time (for node health checks)
- High Waiting Time (for node failure declaration)

### 3.2 Joining a System
New nodes connect via the bootstrap server. The process involves:
- Sending a **HAIL** message to the bootstrap server
- Receiving a port number for further communication or `-1` if it’s the first node
- Sending a **NEW_NODE** message to establish connection with the system

### 3.3 Virtual File System
Supported CLI commands:
- `put [id] [file-path]` – Adds a file to the system
- `get [id]` – Retrieves a file from the system
- `delete [id]` – Deletes a file from the system
- `view-files [address:port]` – Lists all files on a specific node
- `data-info` – Displays node information
- `pause [milliseconds]` – Pauses the node
- `stop` – Stops the node

## 4. Fault Detection
Nodes monitor their successors by sending **HEARTBEAT_REQUEST** messages. If a node doesn’t respond within the **low_waiting_time**, a **RECHECK_NODE** message is sent. If the node still doesn’t respond, it’s considered suspicious and eventually removed after **high_waiting_time**.

## 5. Fault Tolerance
Each file is replicated three times. Nodes store backups, and if a node fails, other nodes retrieve and restore the files. New nodes trigger **REMOVE_BACKUPS** messages to remove outdated backups.

## 6. Messages
Here is a list of messages exchanged between nodes:
- **HAIL** – Sent by a new node to the bootstrap server
- **NEW_NODE** – Sent by nodes to establish connection
- **WELCOME** – Sent to newly joined nodes
- **BACKUP** – Used for file backup
- **PUT** – Stores a file in the system
- **ASK_GET** / **TELL_GET** – Used to retrieve files
- **ASK_DELETE** / **TELL_DELETE** – Used to delete files
- **ASK_VIEW_FILES** / **TELL_VIEW_FILES** – Lists files on a node
- **HEARTBEAT_REQUEST** / **HEARTBEAT_RESPONSE** – Used for node health checks
- **REMOVE_NODE** – Informs nodes of a removal
- **LOAD_BACKUPS** – Loads backups after a node removal
- **REMOVE_BACKUPS** – Removes outdated backups

