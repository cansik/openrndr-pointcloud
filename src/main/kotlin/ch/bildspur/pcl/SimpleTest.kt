package ch.bildspur.pcl

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector3
import org.openrndr.math.transforms.transform

fun main(args: Array<String>) {
    var numFrames = 0
    var pointCount = 1000 * 1000

    application {
        configure {
            width = 770
            height = 578
            title = "PCL"
        }
        program {
            // -- create the vertex buffer
            val geometry = vertexBuffer(vertexFormat {
                position(3)
            }, 4)

            // -- fill the vertex buffer with vertices for a unit quad
            geometry.put {
                write(Vector3(-1.0, -1.0, 0.0))
                write(Vector3(-1.0, 1.0, 0.0))
                write(Vector3(1.0, -1.0, 0.0))
                write(Vector3(1.0, 1.0, 0.0))
            }

            // -- create the secondary vertex buffer, which will hold transformations
            val transforms = vertexBuffer(vertexFormat {
                attribute("transform", VertexElementType.MATRIX44_FLOAT32)
            }, pointCount)

            // -- fill the transform buffer
            transforms.put {
                for (i in 0 until pointCount) {
                    write(transform {
                        translate(Math.random() * width, Math.random() * height)
                        //rotate(Vector3.UNIT_Z, Math.random() * 360.0)
                        //scale(Math.random() * 1.0)
                    })
                }
            }
            extend {
                application.windowTitle = "FPS: ${numFrames / seconds}"
                numFrames++

                drawer.fill = ColorRGBa.WHITE.opacify(0.25)
                drawer.shadeStyle = shadeStyle {
                    vertexTransform = "x_viewMatrix = x_viewMatrix * i_transform;"
                }
                drawer.vertexBufferInstances(listOf(geometry), listOf(transforms), DrawPrimitive.TRIANGLE_STRIP, pointCount)
            }
        }
    }
}