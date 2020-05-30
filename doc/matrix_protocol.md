# Phawtrix documentation

## Matrix initialization
After first connection to the power, an ad-hoc wifi is set up and can be connected to. It is unprotected and has the name `PhAwtrix Controller`, accessible with the password `phawtrixxx`.

Here you need to enter a few settings:

* IP of your MQTT Broker or (the awtrix server)
* The port of the awtrix server
* Define if your have Matrix *Type 1* or *Type 2*, just go with the defaults
* Check if you are connecting to your matrix with an USB connection

## Matrix communication protocol

The matrix is steered from the server software via MQTT messages. So this means you can essentially display anything on the matrix from any device, connected to the same MQTT broker. As long as you publish the right things on the right topic.

The matrix is subscribed to the topic `awtrixmatrix/#` and itself publishes on the topic `matrixClient` (So your server software should subscribe to this channel).

The matrix expects messages in the format of byte arrays, where the first byte of a message contains the actual command to the matrix, what todo.

| Byte   | Command       | uses coordinates |               Description |
| :----- | ------------- | ---------------- | ------------------------: |
| `0x00` | DrawText      | x                |                           |
| `0x01` | DrawBMP       | x                |                           |
| `0x02` | DrawCircle    | x                |                           |
| `0x03` | FillCircle    | x                |                           |
| `0x04` | DrawPixel     | x                |                           |
| `0x05` | DrawRect      | x                |                           |
| `0x06` | DrawLine      | x  (two pairs)   |                           |
| `0x07` | FillMatrix    |                 |                           |
| `0x08` | Show          |                  |                           |
| `0x09` | Clear         |                  |                           |
| `0x10` | PlayMP3       |                 |                           |
| `0x11` | reset         |                  |                           |
| `0x12` | GetMatrixInfo |                  |                           |
| `0x13` | SetBrightness |                  |                           |
| `0x14` | SaveConfig    |                  |                           |
| `0x15` | ResetWifi     |                  |                           |
| `0x16` | Ping          |                  | Send a ping to the server |


## Encoding of coordinates

The coordinates, of where the "cursor" is moved to start printing a payload, are encoded as bytes in the positions:

* **x:** Positions 1 and 2
* **y:** Positions 3 and 4

For each coordinate the formula to get to the actual int value is: `coordinate = int(payload[1] << 8) + int(payload[2])`.

This essentially means that the first payload byte is shifted 8 bit to the left, with zeroes being filled on the right, followed by a conversion to an int. The second payload byte is more clear. Here the raw byte value is converted to an int.

## Command structure

### DrawBmp (0x01)

|byteIndex|content|Description|
|-|-|-|
|0|0x01|command|
|1|coordinate| coordinate x|
|2|coordinate| coordinate x|
|3|coordinate| coordinate y|
|4|coordinate| coordinate y|
|5|width| width|
|6|height|height|
|7 ....| color bytes| color bytes pairwise|
